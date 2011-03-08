package org.isolution.nexus.gateway;

import org.apache.log4j.Logger;
import org.isolution.nexus.domain.Endpoint;
import org.isolution.nexus.domain.ServiceURI;
import org.isolution.nexus.routing.ServiceRouter;
import org.isolution.nexus.xml.SOAPDocument;
import org.springframework.ws.server.endpoint.AbstractStaxStreamPayloadEndpoint;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 * User: agwibowo
 * Date: 26/12/10
 * Time: 11:21 PM
 */
public class NexusGatewayEndpoint extends AbstractStaxStreamPayloadEndpoint {
    public static final Logger LOGGER = Logger.getLogger(NexusGatewayEndpoint.class);

    private ServiceRouter serviceRouter;

    public NexusGatewayEndpoint() {
    }

    @Override
    protected void invokeInternal(XMLStreamReader streamReader, XMLStreamWriter streamWriter) throws Exception {
        SOAPDocument soapDocument = new SOAPDocument(streamReader);
        ServiceURI serviceURI = soapDocument.getServiceURI();
        Endpoint targetEndpoint = serviceRouter.findSingleActiveEndpoint(serviceURI.toString());
        // invoke endpoint with soapDocument
    }
}
