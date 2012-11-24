package org.isolution.nexus.gateway;

import com.sun.net.httpserver.HttpServer;
import org.isolution.nexus.domain.Endpoint;
import org.isolution.nexus.domain.Service;
import org.isolution.nexus.domain.ServiceURI;
import org.isolution.nexus.domain.Status;
import org.isolution.nexus.domain.dao.EndpointDAO;
import org.isolution.nexus.domain.dao.ServiceDAO;
import org.isolution.nexus.test.support.AbstractTransactionalTest;
import org.isolution.nexus.test.support.EchoHttpServer;
import org.isolution.nexus.test.support.EndpointBuilder;
import org.isolution.nexus.test.support.ServiceBuilder;
import org.isolution.nexus.xml.SOAPDocumentFactory;
import org.isolution.nexus.xml.soap.SOAPMessageSuite;
import org.isolution.nexus.xml.soap.SOAPMessageUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.SoapMessageFactory;
import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.http.CommonsHttpMessageSender;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * User: Alex Wibowo
 * Date: 15/08/11
 * Time: 11:28 PM
 */
@ContextConfiguration(locations = {"classpath:spring-test-ws-servlet.xml"})
public class NexusGatewayEndpointAcceptanceTest extends AbstractTransactionalTest{

    @Autowired
    private SoapMessageFactory soapMessageFactory;

    @Autowired
    private ServiceDAO serviceDAO;

    @Autowired
    private EndpointDAO endpointDAO;

    @Autowired
    private SOAPDocumentFactory soapDocumentFactory;

    private HttpServer server;

    private String expectedResponse;

    @Before
    public void setup() throws IOException, SAXException, ParserConfigurationException {
        SoapMessage expectedSoapResponse = soapDocumentFactory.createSoapMessage(SOAPMessageSuite.SOAP11ResponseFixture.getSOAPStr());
        expectedResponse = new SOAPMessageUtil().convertToString(expectedSoapResponse);
        server = EchoHttpServer.createServer(9000, SOAPMessageSuite.SOAP11ResponseFixture.getSOAPStr(), "/echo");
    }

    @After
    public void stop(){
        server.stop(2);
    }

    @Test
    public void shouldRouteMessageToTheCorrectEndpoint() throws Exception{
        SOAPMessageSuite.Fixture requestMessageFixture = SOAPMessageSuite.SOAP11RequestFixture;
        prepareData(requestMessageFixture);

        // run test
        WebServiceMessage soapRequest = soapDocumentFactory.createSoapMessage(requestMessageFixture.getSOAPStr());
        WebServiceMessage soapResponse = sendRequest(soapRequest);

        String actualResponse = new SOAPMessageUtil().convertToString(soapResponse);
        assertThat(actualResponse, equalTo(expectedResponse));
    }

    private WebServiceMessage sendRequest(WebServiceMessage soapRequest) throws IOException, URISyntaxException {
        WebServiceConnection connection = getConnection();
        connection.send(soapRequest);

        assertThat("This request is not expected to fail, but an error [" + connection.getErrorMessage() + "] was received", connection.hasError(), is(false));
        WebServiceMessage response = connection.receive(soapMessageFactory);
        connection.close();
        return response;
    }

    private void prepareData(SOAPMessageSuite.Fixture requestMessageFixture) throws InvocationTargetException, IllegalAccessException {
        endpointDAO.deleteAll();
        serviceDAO.deleteAll();

        sessionFactory.getCurrentSession().flush();


        Endpoint endpoint = new EndpointBuilder().withUri("http://localhost:9000/echo").build();
        endpoint = endpointDAO.save(endpoint);

        ServiceURI serviceURI = new ServiceURI(requestMessageFixture.getNamespace(), requestMessageFixture.getLocalName());

        Service service = new ServiceBuilder().withServiceURI(serviceURI).build();
        service = serviceDAO.save(service);
        service.addEndpoint(endpoint, Status.ACTIVE);
        service = serviceDAO.save(service);
//
        commitAndRenewTransaction();
    }

    private WebServiceConnection getConnection()
            throws IOException, URISyntaxException {
        CommonsHttpMessageSender sender = new CommonsHttpMessageSender();
        return sender.createConnection(new URI("http://localhost:8080/nexus/"));
    }

}
