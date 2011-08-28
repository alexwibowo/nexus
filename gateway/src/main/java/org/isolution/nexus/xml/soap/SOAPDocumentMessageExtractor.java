package org.isolution.nexus.xml.soap;

import org.isolution.nexus.xml.SOAPDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.xml.StaxUtils;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageExtractor;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.SoapMessageFactory;
import org.springframework.ws.soap.axiom.AxiomSoapMessage;

import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * Implementation of {@link WebServiceMessageExtractor} which returns an instance of {@link SOAPDocument} from
 * the {@link WebServiceMessage} that it receives.
 * <p/>
 * User: Alex Wibowo
 * Date: 28/08/11
 * Time: 10:37 AM
 */
@Component
public class SOAPDocumentMessageExtractor implements WebServiceMessageExtractor<SOAPDocument>{

    private SoapMessageFactory soapMessageFactory;

    @Autowired
    public SOAPDocumentMessageExtractor(SoapMessageFactory soapMessageFactory) {
        this.soapMessageFactory = soapMessageFactory;
    }

    @Override
    public SOAPDocument extractData(WebServiceMessage message)
            throws IOException, TransformerException {
        XMLStreamReader xmlStreamReader = StaxUtils.getXMLStreamReader(message.getPayloadSource());

        SoapMessage webServiceMessage = soapMessageFactory.createWebServiceMessage();
        webServiceMessage.setDocument(((AxiomSoapMessage) message).getDocument());

        return new SOAPDocument(webServiceMessage, xmlStreamReader);
    }
}
