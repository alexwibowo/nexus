package org.isolution.xml.soap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.*;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;

/**
 * {@link SOAPMessage} builder, e.g. SOAP message with payload of JAXB object, or simply creating SOAP message
 * from String
 */
public class SOAPMessageBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(SOAPMessageBuilder.class.getName());

    private final JAXBContext jaxbContext;

    private String soapVersion;

    private boolean formatPayloadOutput = false;

    private final Object payload;

    private SOAPMessageBuilder(JAXBContext jaxbContext, Object payload) {
        this.jaxbContext = jaxbContext;
        this.payload = payload;
    }

    /**
     * Create the default builder, with the given JAXB object. This method will create, by default:
     * 1. SOAP 1.2 message
     * 2. Formatted SOAP message
     *
     * @param context {@link JAXBContext}
     * @param payload jaxb object that can be marshalled as the SOAP body payload
     * @see #asSoap12()
     * @see #asSoap11()
     * @see #asFormattedPayload()
     * @see #asUnformattedPayload()
     * @return this builder instance
     */
    public static SOAPMessageBuilder getDefault(JAXBContext context, Object payload) {
        SOAPMessageBuilder builder = new SOAPMessageBuilder(context, payload);
        builder.asSoap12().asFormattedPayload();
        return builder;
    }

    /**
     * Create a SOAPMessage from the given string. This method will investigate the namespace
     * inside the SOAP string, to determine whether to create SOAP 1.1 or SOAP 1.2.
     *
     * @param soapStr
     * @return SOAPMessage object
     * @see SOAPConstants#URI_NS_SOAP_1_1_ENVELOPE
     * @see SOAPConstants#URI_NS_SOAP_1_2_ENVELOPE
     * @throws SOAPException
     */
    public static SOAPMessage fromSOAPString(String soapStr) throws SOAPException {
        String soapVersion = SOAPConstants.SOAP_1_2_PROTOCOL;
        if (StringUtils.contains(soapStr, SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE)) {
            LOGGER.trace("Creating SOAP 1.1 message");
            soapVersion = SOAPConstants.SOAP_1_1_PROTOCOL;
        }

        MessageFactory messageFactory = MessageFactory.newInstance(soapVersion);
        SOAPMessage message = messageFactory.createMessage();

        StreamSource streamSource = new StreamSource(new ByteArrayInputStream(soapStr.getBytes()));
        message.getSOAPPart().setContent(streamSource);

        return message;
    }


    public SOAPMessageBuilder asSoap11() {
        soapVersion = SOAPConstants.SOAP_1_1_PROTOCOL;
        return this;
    }

    public SOAPMessageBuilder asSoap12() {
        soapVersion = SOAPConstants.SOAP_1_2_PROTOCOL;
        return this;
    }

    public SOAPMessageBuilder asFormattedPayload() {
        formatPayloadOutput = true;
        return this;
    }

    public SOAPMessageBuilder asUnformattedPayload() {
        formatPayloadOutput = false;
        return this;
    }

    public SOAPMessage build() {
        try {
            StringWriter stringWriter = new StringWriter();

            LOGGER.debug("Marshalling payload of type [{}]", payload.getClass().getName());
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formatPayloadOutput);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            marshaller.marshal(payload, stringWriter);


            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();

            LOGGER.debug("Constructing Document from payload string");
            Document payloadDocument = documentBuilder.parse(new ByteArrayInputStream(stringWriter.toString().getBytes()));

            LOGGER.debug("Creating SOAP message with Document payload");
            MessageFactory messageFactory = MessageFactory.newInstance(soapVersion);
            SOAPMessage message = messageFactory.createMessage();
            SOAPBody soapBody = message.getSOAPBody();
            soapBody.addDocument(payloadDocument);

            return message;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }





}
