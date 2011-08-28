package org.isolution.nexus.invoker.impl;

import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.util.stax.XMLStreamReaderUtils;
import org.apache.log4j.Logger;
import org.isolution.nexus.domain.EndpointProtocol;
import org.isolution.nexus.invoker.Invoker;
import org.isolution.nexus.xml.SOAPDocument;
import org.isolution.nexus.xml.SOAPDocumentFactory;
import org.isolution.nexus.xml.soap.SOAPDocumentMessageExtractor;
import org.isolution.nexus.xml.soap.SOAPMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.xml.StaxUtils;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.SourceExtractor;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceMessageExtractor;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.SoapMessageCreationException;
import org.springframework.ws.soap.SoapMessageFactory;
import org.springframework.ws.soap.axiom.AxiomSoapMessage;
import org.springframework.xml.transform.TransformerHelper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URI;
import java.rmi.server.UID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * User: Alex Wibowo
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

    private WebServiceMessageExtractor<SOAPDocument> soapDocumentMessageExtractor;

    @Autowired
    public HTTPInvoker(WebServiceTemplate webServiceTemplate, WebServiceMessageExtractor<SOAPDocument> soapDocumentMessageExtractor) {
        this.webServiceTemplate = webServiceTemplate;
        this.soapDocumentMessageExtractor = soapDocumentMessageExtractor;
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
    public SOAPDocument invoke(final SOAPDocument document)
            throws IOException {
//        try {
            LOGGER.debug(String.format("About to invoke [%s]", uri.toString()));
//            webServiceTemplate.sendSourceAndReceiveToResult(uri.toString(),
//                    document.getRawSoapMessage().getPayloadSource(),
//                    new StreamResult(responseWriter));

//            TransformerHelper transformerHelper = new TransformerHelper();
//            final Transformer transformer = transformerHelper.createTransformer();

//            createRequestCallback(document, transformer);


            return  webServiceTemplate.sendAndReceive(uri.toString(), null, soapDocumentMessageExtractor);

//            return webServiceTemplate.sendSourceAndReceive(uri.toString(),
//                    document.getRawSoapMessage().getPayloadSource(),
//                    new SourceExtractor<SOAPDocument>() {
//                        @Override
//                        public SOAPDocument extractData(Source source) throws IOException, TransformerException {
//                            try {
//                                XMLStreamReader xmlStreamReader = StaxUtils.getXMLStreamReader(source);
//                                String text = new StAXOMBuilder(xmlStreamReader).getDocument().toString();
//
//                                Document soapDocument = new SOAPMessageUtil().createSoapDomDocument(text);
//                                SoapMessage webServiceMessage = soapMessageFactory.createWebServiceMessage();
//                                webServiceMessage.setDocument(soapDocument);
//
//                                return new SOAPDocument(webServiceMessage, xmlStreamReader);
//                            } catch (Exception e) {
//                                throw new RuntimeException(e);
//                            }
//                        }
//                    });
//            return soapDocumentFactory.createSoapDomDocument(responseWriter.toString());
//        } catch (Exception e) {
//            String message = "An error had occurred while creating SOAP response from [%s]";
//            LOGGER.error(String.format(message, uri.toString()), e);
//            throw new SoapMessageCreationException(message, e);
//        }
    }

    private void createRequestCallback(final SOAPDocument document, final Transformer transformer) {
        WebServiceMessageCallback requestCallback = new WebServiceMessageCallback() {
            @Override
            public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {
                transformer.transform(document.getRawSoapMessage().getPayloadSource(), message.getPayloadResult());
            }
        };
    }
//
//    private WebServiceMessageExtractor<SOAPDocument> createResponseExtractor() {
//        return new WebServiceMessageExtractor<SOAPDocument>() {
//                    @Override
//                    public SOAPDocument extractData(WebServiceMessage message) throws IOException, TransformerException {
//                        XMLStreamReader xmlStreamReader = StaxUtils.getXMLStreamReader(message.getPayloadSource());
//
//                        SoapMessage webServiceMessage = soapMessageFactory.createWebServiceMessage();
//                        webServiceMessage.setDocument(((AxiomSoapMessage) message).getDocument());
//                        return new SOAPDocument(webServiceMessage, xmlStreamReader);
//                    }
//                };
//    }
}
