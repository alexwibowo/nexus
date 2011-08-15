package org.isolution.nexus.gateway;

import org.isolution.nexus.xml.SOAPDocument;
import org.springframework.stereotype.Component;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapMessage;

import javax.xml.stream.XMLStreamReader;

/**
 * User: agwibowo
 * Date: 12/08/11
 * Time: 12:03 PM
 */
@Component
class NexusGatewayEndpointHelper {

    public SOAPDocument getSOAPDocument(MessageContext messageContext, XMLStreamReader payloadStreamReader) {
        SoapMessage soapMessage = (SoapMessage) messageContext.getRequest();
        return new SOAPDocument(soapMessage, payloadStreamReader);
    }
}
