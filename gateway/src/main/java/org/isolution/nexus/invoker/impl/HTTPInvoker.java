package org.isolution.nexus.invoker.impl;

import org.apache.log4j.Logger;
import org.isolution.nexus.domain.EndpointProtocol;
import org.isolution.nexus.invoker.Invoker;
import org.isolution.nexus.xml.SOAPDocument;
import org.isolution.nexus.xml.SOAPDocumentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessageCreationException;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * User: agwibowo
 * Date: 19/04/11
 * Time: 10:20 PM
 */
@Component
@Qualifier("http")
public class HTTPInvoker implements Invoker<URI> {
    public static final Logger LOGGER = org.apache.log4j.Logger.getLogger(HTTPInvoker.class);

    private URI uri;

    private final EndpointProtocol supportedProtocol = EndpointProtocol.HTTP;

    private WebServiceTemplate webServiceTemplate;

    private SOAPDocumentFactory soapDocumentFactory;

    @Autowired
    public HTTPInvoker(WebServiceTemplate webServiceTemplate, SOAPDocumentFactory soapDocumentFactory) {
        this.webServiceTemplate = webServiceTemplate;
        this.soapDocumentFactory = soapDocumentFactory;
    }

    @Override
    public boolean supports(EndpointProtocol protocol) {
        return protocol.equals(supportedProtocol);
    }

    @Override
    public void setTarget(URI target) {
        checkNotNull(target);
        this.uri = target;
    }

    @Override
    public SOAPDocument invoke(SOAPDocument document)
            throws IOException {
        try {
            Writer responseWriter = new StringWriter();
            LOGGER.debug(String.format("About to invoke [%s]", uri.toString()));
            webServiceTemplate.sendSourceAndReceiveToResult(uri.toString(),
                    document.getRawSoapMessage().getPayloadSource(),
                    new StreamResult(responseWriter));

            return soapDocumentFactory.createSOAPResponse(responseWriter.toString());
        } catch (XMLStreamException e) {
            String message = "An error had occurred while creating SOAP response from [%s]";
            LOGGER.error(String.format(message, uri.toString()), e);
            throw new SoapMessageCreationException(message, e);
        }
    }
}
