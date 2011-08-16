package org.isolution.nexus.gateway;

import com.sun.net.httpserver.*;
import org.hibernate.SessionFactory;
import org.isolution.nexus.domain.*;
import org.isolution.nexus.domain.dao.EndpointDAO;
import org.isolution.nexus.domain.dao.ServiceDAO;
import org.isolution.nexus.xml.soap.SOAPMessageSuite;
import org.isolution.nexus.xml.soap.SOAPMessageUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.SoapMessageFactory;
import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.WebServiceMessageSender;
import org.springframework.ws.transport.http.CommonsHttpMessageSender;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.*;
import java.sql.Timestamp;

/**
 * User: agwibowo
 * Date: 15/08/11
 * Time: 11:28 PM
 */
@ContextConfiguration(locations = {"classpath:spring-test-ws-servlet.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class NexusGatewayEndpointAcceptanceTest extends AbstractTransactionalJUnit4SpringContextTests{

    @Autowired
    private SoapMessageFactory soapMessageFactory;

    @Autowired
    private ServiceDAO serviceDAO;

    @Autowired
    private EndpointDAO endpointDAO;

    @Autowired
    private WebServiceTemplate webServiceTemplate;

    @Autowired
    private SessionFactory sessionFactory;

    private HttpServer server;

    @Before
    public void setup() throws IOException {
        String response = SOAPMessageSuite.SOAP11ResponseFixture.getSOAPStr();
        server = createServer(9000,  response);
    }

    @After
    public void stop(){
        server.stop(10);
    }

    @Test
    public void shouldRouteMessageToTheCorrectEndpoint() throws XMLStreamException, IOException, SAXException, ParserConfigurationException, URISyntaxException, InterruptedException {
//        endpointDAO.deleteAll();
//        serviceDAO.deleteAll();
//        sessionFactory.getCurrentSession().flush();

        SOAPMessageSuite.Fixture requestMessageFixture = SOAPMessageSuite.SOAP11RequestFixture;
//
//        Endpoint endpoint = new Endpoint();
//        endpoint.setProtocol(EndpointProtocol.HTTP);
//        endpoint.setStatus(Status.ACTIVE);
//        Timestamp now = new Timestamp(System.currentTimeMillis());
//        endpoint.setCreateDateTime(now);
//        endpoint.setUpdateDateTime(now);
//        endpoint.setUri("http://localhost:3000");
//        endpoint = endpointDAO.save(endpoint);
//
//        ServiceURI serviceURI = new ServiceURI(requestMessageFixture.getNamespace(), requestMessageFixture.getLocalName());
//        Service service = new Service();
//        service.setServiceURI(serviceURI);
//        service.setCreateDateTime(now);
//        service.setUpdateDateTime(now);
//        service.setStatus(Status.ACTIVE);
//
//        service = serviceDAO.save(service);
//        service.addEndpoint(endpoint, Status.ACTIVE);
//        service = serviceDAO.save(service);
//
//        sessionFactory.getCurrentSession().getTransaction().commit();

        Document soapDocument = new SOAPMessageUtil().getSOAPDocument(requestMessageFixture.getSOAPStr());
        SoapMessage webServiceMessage = soapMessageFactory.createWebServiceMessage();
        webServiceMessage.setDocument(soapDocument);


        Writer responseWriter = new StringWriter();
        webServiceTemplate.sendSourceAndReceiveToResult("http://localhost:8080/echo",
                webServiceMessage.getPayloadSource(),
                new StreamResult(responseWriter));
        System.out.println(responseWriter.toString());
//        getConnection().send(webServiceMessage);
    }

    private HttpServer createServer(int port, final String staticResponse) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(port), 9);
        HttpContext context = httpServer.createContext("/echo");
        context.setHandler(new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,  staticResponse.length());

                OutputStream responseBody = httpExchange.getResponseBody();
                ((OutputStream) responseBody).write(staticResponse.getBytes());
                responseBody.flush();
                responseBody.close();
            }
        });
        httpServer.start();
        return httpServer;
    }

    private WebServiceConnection getConnection()
            throws IOException, URISyntaxException {
        WebServiceMessageSender sender = new CommonsHttpMessageSender();
        return sender.createConnection(new URI("http://localhost:9000/echo"));
    }

}
