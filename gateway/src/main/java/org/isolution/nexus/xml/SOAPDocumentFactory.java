package org.isolution.nexus.xml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.SoapMessageFactory;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;

import static org.apache.commons.io.IOUtils.toInputStream;

/**
 * User: agwibowo
 * Date: 13/07/11
 * Time: 9:47 PM
 */
@Component
public class SOAPDocumentFactory {
    private SoapMessageFactory soapMessageFactory;

    private XMLInputFactory xmlInputFactory;

    @Autowired
    public SOAPDocumentFactory(SoapMessageFactory soapMessageFactory, XMLInputFactory xmlInputFactory) {
        this.soapMessageFactory = soapMessageFactory;
        this.xmlInputFactory = xmlInputFactory;
    }

    public SOAPDocument createSOAPResponse(final String responseString)
            throws IOException, XMLStreamException {
        SoapMessage responseMessage = soapMessageFactory.createWebServiceMessage(toInputStream(responseString));
        return new SOAPDocument(responseMessage, xmlInputFactory.createXMLStreamReader(toInputStream(responseString)));
    }
}
