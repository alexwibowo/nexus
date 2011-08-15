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

    private NexusGatewayEndpointHelper helper;

    public NexusGatewayEndpoint(ServiceRouter serviceRouter, InvokerResolver invokerResolver, NexusGatewayEndpointHelper helper) {
        this.serviceRouter = serviceRouter;
        this.invokerResolver = invokerResolver;
        this.helper = helper;
    }

    /**
     * @param payloadStreamReader the reader to read the payload from
     * @param payloadStreamWriter the writer to write the payload to
     * @throws Exception
     */
    @Override
    protected void invokeInternal(XMLStreamReader payloadStreamReader, XMLStreamWriter payloadStreamWriter) throws Exception {
        SOAPDocument request = helper.getSOAPDocument(MessageContextHolder.getMessageContext(), payloadStreamReader);

        ServiceURI serviceURI = request.getServiceURI();

        Endpoint targetEndpoint = serviceRouter.findSingleActiveEndpoint(serviceURI.getServiceURIString());

        Invoker invoker = invokerResolver.resolveForEndpoint(targetEndpoint);

        SOAPDocument response = invoker.invoke(request);
        response.writeTo(payloadStreamWriter);
    }
}
