package org.isolution.nexus.gateway;

import org.apache.log4j.Logger;
import org.isolution.nexus.domain.Endpoint;
import org.isolution.nexus.domain.ServiceURI;
import org.isolution.nexus.invoker.Invoker;
import org.isolution.nexus.invoker.InvokerResolver;
import org.isolution.nexus.routing.ServiceRouter;
import org.isolution.nexus.xml.SOAPDocument;
import org.isolution.springframework.ws.MessageContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.AbstractStaxStreamPayloadEndpoint;
import org.springframework.ws.soap.SoapMessage;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.net.URI;

/**
 * User: agwibowo
 * Date: 26/12/10
 * Time: 11:21 PM
 */
public class NexusGatewayEndpoint extends AbstractStaxStreamPayloadEndpoint {
    public static final Logger LOGGER = Logger.getLogger(NexusGatewayEndpoint.class);

    private ServiceRouter serviceRouter;

    private InvokerResolver invokerResolver;

    @Autowired
    public NexusGatewayEndpoint(ServiceRouter serviceRouter, InvokerResolver invokerResolver) {
        this.serviceRouter = serviceRouter;
        this.invokerResolver = invokerResolver;
    }

    /**
     * @param payloadStreamReader the reader to read the payload from
     * @param payloadStreamWriter the writer to write the payload to
     * @throws Exception
     */
    @Override
    protected void invokeInternal(XMLStreamReader payloadStreamReader, XMLStreamWriter payloadStreamWriter) throws Exception {
        SOAPDocument soapDocument = getSOAPDocument(payloadStreamReader);
        ServiceURI serviceURI = soapDocument.getServiceURI();

        Endpoint targetEndpoint = serviceRouter.findSingleActiveEndpoint(serviceURI.toString());

        Invoker<URI> invoker = invokerResolver.resolveForProtocol(targetEndpoint.getProtocol());
        invoker.setTarget(targetEndpoint.toURI());
        invoker.invoke(soapDocument);
    }

    private SOAPDocument getSOAPDocument(XMLStreamReader payloadStreamReader) {
        MessageContext messageContext = MessageContextHolder.getMessageContext();
        SoapMessage soapMessage = (SoapMessage) messageContext.getRequest();
        return new SOAPDocument(soapMessage, payloadStreamReader);
    }
}
