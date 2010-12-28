package org.isolution.nexus.gateway;

import org.apache.log4j.Logger;
import org.isolution.nexus.domain.ServiceURI;
import org.isolution.nexus.xml.XMLUtil;
import org.isolution.nexus.xml.soap.SOAPMessageUtil;
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

    private XMLUtil xmlUtil;

    public NexusGatewayEndpoint(XMLUtil xmlUtil) {
        this.xmlUtil = xmlUtil;
    }

    @Override
    protected void invokeInternal(XMLStreamReader streamReader, XMLStreamWriter streamWriter) throws Exception {
        ServiceURI serviceURI = xmlUtil.getServiceURI(streamReader);
    }
}
