package org.isolution.xml.soap;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMSourcedElement;
import org.apache.axiom.om.ds.jaxb.JAXBOMDataSource;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPHeader;
import org.isolution.security.SecurityHelper;
import org.isolution.security.SecurityToken;
import org.isolution.security.SecurityTokenGenerator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * User: alexwibowo
 */
public class AxiomSOAP11MessageBuilder {


    private Object payloadDocument;

    private final JAXBContext jaxbContext;

    private final SOAPEnvelope envelope;
    private final SOAPHeader soapHeader;

    private static final SOAPFactory soap11Factory;

    static {
        soap11Factory = OMAbstractFactory.getSOAP11Factory();
    }

    public AxiomSOAP11MessageBuilder(JAXBContext jaxbContext) {
        this.jaxbContext = jaxbContext;

        envelope = soap11Factory.getDefaultEnvelope();
        soapHeader = envelope.getHeader();
    }

    public SOAPHeader getSoapHeader() {
        return soapHeader;
    }

    public AxiomSOAP11MessageBuilder withPayload(Object object) {
        checkNotNull(object);
        payloadDocument = object;
        return this;
    }

    public AxiomSOAP11MessageBuilder addHeader(JAXBElement header) {
        JAXBOMDataSource jaxbomDataSource = new JAXBOMDataSource(jaxbContext, header);
        OMSourcedElement omElement = soap11Factory.createOMElement(jaxbomDataSource);
        soapHeader.addChild(omElement);
        return this;
    }

    public AxiomSOAP11MessageBuilder withSecurityHeader(String username, String sharedKey) throws SOAPException {
        checkArgument(isNotBlank(username));
        checkArgument(isNotBlank(sharedKey));

        OMElement securityElement = soap11Factory.createOMElement("Security", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "wsse");
        soapHeader.addChild(securityElement);


        OMElement usernameToken = soap11Factory.createOMElement("UsernameToken", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "wsse");
        securityElement.addChild(usernameToken);

        OMElement usernameElement = soap11Factory.createOMElement("Username", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "wsse");
        usernameElement.setText(username);
        usernameToken.addChild(usernameElement);

        SecurityTokenGenerator securityTokenGenerator = new SecurityTokenGenerator(new SecurityHelper(), sharedKey);
        SecurityToken generatedToken = securityTokenGenerator.generate();

        String timestamp = generatedToken.getTimestamp();

        OMElement createdElement = soap11Factory.createOMElement("Created", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "wsse");
        createdElement.setText(timestamp);
        usernameToken.addChild(createdElement);

        OMElement nonceElement = soap11Factory.createOMElement("Nonce", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "wsse");
        nonceElement.setText(generatedToken.getNonce());
        usernameToken.addChild(nonceElement);

        OMElement passwordElement = soap11Factory.createOMElement("Password", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "wsse");
        passwordElement.setText(generatedToken.getNonce());

        passwordElement.addAttribute(soap11Factory.createOMAttribute("Type", passwordElement.getNamespace(), "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest"));
        passwordElement.addAttribute(soap11Factory.createOMAttribute("EncodingType", passwordElement.getNamespace(), "SHA-1"));
        passwordElement.setText(generatedToken.getMessageDigest());
        usernameToken.addChild(passwordElement);

        return this;
    }

    public SOAPEnvelope build() {
        org.apache.axiom.soap.SOAPBody body = envelope.getBody();

        XmlRootElement annotation = payloadDocument.getClass().getAnnotation(XmlRootElement.class);
        if (annotation != null) {
            JAXBOMDataSource jaxbomDataSource = new JAXBOMDataSource(jaxbContext, payloadDocument);
            OMSourcedElement omElement = soap11Factory.createOMElement(jaxbomDataSource);
            body.addChild(omElement);
        }else if (payloadDocument instanceof  OMElement) {
            OMElement omElementPayload = (OMElement) payloadDocument;
            body.addChild(omElementPayload);
        }
        return envelope;
    }

    public String getContentType() {
        return SOAPConstants.SOAP_1_1_CONTENT_TYPE;
    }
}
