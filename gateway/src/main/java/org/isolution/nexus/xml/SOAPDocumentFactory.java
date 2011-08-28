package org.isolution.nexus.xml;

import org.isolution.nexus.xml.soap.SOAPMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.SoapMessageFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;

import static org.apache.commons.io.IOUtils.toInputStream;

/**
 * User: Alex Wibowo
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

    public SOAPDocument createSOAPDocument(final String soapString)
            throws IOException, XMLStreamException, SAXException, ParserConfigurationException {
        SoapMessage soapMessage = createSoapMessage(soapString);
        return new SOAPDocument(soapMessage, xmlInputFactory.createXMLStreamReader(toInputStream(soapString)));
    }

    /**
     * Create {@link SoapMessage} from the given SOAP String.
     */
    public SoapMessage createSoapMessage(String soapStr)
            throws IOException, SAXException, ParserConfigurationException {
        SoapMessage soapRequest = soapMessageFactory.createWebServiceMessage();
        Document soapDocument = new SOAPMessageUtil().createSoapDomDocument(soapStr);
        soapRequest.setDocument(soapDocument);
        return soapRequest;
    }
}
