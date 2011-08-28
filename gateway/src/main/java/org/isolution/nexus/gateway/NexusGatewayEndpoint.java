package org.isolution.nexus.gateway;

import org.isolution.nexus.invoker.InvokerController;
import org.isolution.nexus.xml.SOAPDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.server.endpoint.AbstractStaxStreamPayloadEndpoint;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import static org.isolution.springframework.ws.MessageContextHolder.getMessageContext;

/**
 * The main endpoint - this is where everything starts. Receives SOAP messages & respond to them.
 * <p/>
 * User: Alex Wibowo
 * Date: 26/12/10
 * Time: 11:21 PM
 */
public class NexusGatewayEndpoint extends AbstractStaxStreamPayloadEndpoint {
    public static final Logger LOGGER = LoggerFactory.getLogger(NexusGatewayEndpoint.class);

    private InvokerController controller;

    private NexusGatewayEndpointHelper helper;

    public NexusGatewayEndpoint(InvokerController controller, NexusGatewayEndpointHelper helper) {
        this.helper = helper;
        this.controller = controller;
    }

    /**
     * @param payloadStreamReader the reader to read the payload from
     * @param payloadStreamWriter the writer to write the payload to
     * @throws Exception
     */
    @Override
    protected void invokeInternal(XMLStreamReader payloadStreamReader, XMLStreamWriter payloadStreamWriter)
            throws Exception {
        LOGGER.info("Received message");

        SOAPDocument request = helper.getSOAPDocument(getMessageContext(), payloadStreamReader);
        LOGGER.debug("Request is {}", request);

        SOAPDocument response = controller.invoke(request);
        LOGGER.debug("Response is {}", response);

        getMessageContext().setResponse(response.getRawSoapMessage());
        LOGGER.info("Finished handling message");
    }
}
