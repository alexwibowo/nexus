package org.isolution.nexus.xml.soap;

import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.util.StAXUtils;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPVersion;
import org.apache.axiom.soap.impl.builder.StAXSOAPModelBuilder;
import org.apache.commons.lang.StringUtils;

import javax.xml.soap.SOAPException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A collection of utility methods for operations around SOAP message
 *
 * User: agwibowo
 * Date: 14/12/10
 * Time: 10:42 PM
 */
public class SOAPMessageUtil {

    /**
     * Get the namespace of the given SOAP message. In the case where the SOAP message has multiple children inside its SOAP body, only
     * the first child's namespace is returned.
     *
     * @param soapMessageStr string representation of the SOAP message
     * @return namespace for the SOAP message
     * @throws SOAPException      if the SOAP body doesnt have any children
     * @throws XMLStreamException any error that occurred during the XML processing of the SOAP message
     */
    public String getNamespace(String soapMessageStr) throws SOAPException, XMLStreamException {
        SOAPEnvelope soapEnvelope = getSOAPEnvelope(soapMessageStr);
        OMNamespace soapBodyFirstElementNS = soapEnvelope.getSOAPBodyFirstElementNS();
        if (soapBodyFirstElementNS == null) {
            throw new SOAPException("Empty SOAP Body");
        }
        return soapBodyFirstElementNS.getNamespaceURI();
    }

    /**
     * Get the SOAP version of the given SOAP message
     *
     * @param soapMessageStr String representation of the SOAP message
     * @return {@link SOAPVersion} for the SOAP message
     * @throws XMLStreamException any error that occurred during the XML processing of the SOAP message
     */
    public SOAPVersion getSOAPVersion(String soapMessageStr) throws XMLStreamException {
        SOAPEnvelope soapEnvelope = getSOAPEnvelope(soapMessageStr);
        return soapEnvelope.getVersion();
    }

    private SOAPEnvelope getSOAPEnvelope(String soapMessageStr) throws XMLStreamException {
        checkArgument(!StringUtils.isBlank(soapMessageStr), "SOAP message must be supplied");
        XMLStreamReader xmlStreamReader = StAXUtils.createXMLStreamReader(new StringReader(soapMessageStr));
        StAXSOAPModelBuilder soapModelBuilder = new StAXSOAPModelBuilder(xmlStreamReader);
        return soapModelBuilder.getSOAPEnvelope();
    }
}