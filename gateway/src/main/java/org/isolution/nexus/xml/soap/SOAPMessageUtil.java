package org.isolution.nexus.xml.soap;

import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.util.StAXUtils;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPVersion;
import org.apache.axiom.soap.impl.builder.StAXSOAPModelBuilder;
import org.apache.commons.lang.StringUtils;
import org.isolution.nexus.domain.Service;

import javax.xml.soap.SOAPException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A collection of utility methods for operations around SOAP message
 * <p/>
 * User: agwibowo
 * Date: 14/12/10
 * Time: 10:42 PM
 */
public class SOAPMessageUtil {

    /**
     * Get the namespace of the given SOAP message. The namespace for the SOAP message is deduced from the direct first child of the SOAP message's body element.
     * In the case where the SOAP message has multiple children inside its SOAP body, only the first child's namespace is returned.
     *
     *
     * @param soapMessageStr string representation of the SOAP message
     * @return namespace for the SOAP message
     * @throws SOAPException      if the SOAP body doesnt have any children
     * @throws XMLStreamException any error that occurred during the XML processing of the SOAP message
     */
    public String getNamespace(String soapMessageStr) throws SOAPException, XMLStreamException {
        return getNamespace(getSOAPEnvelope(soapMessageStr));
    }

    /**
     * Get the namespace of the SOAP message that will be read from the given {@link XMLStreamReader}. The namespace for the SOAP message is deduced
     * from the direct first child of the SOAP message's body element.
     *
     * @param streamReader the {@link XMLStreamReader} from which the SOAP message will be read from
     * @return namespace for the SOAP message
     * @throws SOAPException      if the SOAP body doesnt have any children
     */
    public String getNamespace(XMLStreamReader streamReader) throws SOAPException {
        return getNamespace(getSOAPEnvelope(streamReader));
    }

    /**
     * Get the namespace of the given {@link SOAPEnvelope}. The namespace for the SOAP message is deduced from the direct first child of
     * the SOAP message's body element.
     *
     * @param soapEnvelope the {@link SOAPEnvelope} from which the SOAP message will be read from
     * @return namespace for the SOAP message
     * @throws SOAPException      if the SOAP body doesnt have any children
     */
    public String getNamespace(SOAPEnvelope soapEnvelope) throws SOAPException {
        OMNamespace soapBodyFirstElementNS = soapEnvelope.getSOAPBodyFirstElementNS();
        if (soapBodyFirstElementNS == null) {
            throw new SOAPException("Empty SOAP Body");
        }
        return soapBodyFirstElementNS.getNamespaceURI();
    }

    /**
     * @param soapMessageStr string representation of the SOAP message
     * @return service Service for the SOAP message
     * @throws XMLStreamException any error that occurred during the XML processing of the SOAP message
     * @throws SOAPException      if the SOAP has empty body
     * @see org.isolution.nexus.domain.Service
     */
    public Service getServiceURI(String soapMessageStr) throws XMLStreamException, SOAPException {
        return getServiceURI(getSOAPEnvelope(soapMessageStr));
    }

    /**
     * @param streamReader {@link XMLStreamReader} to read SOAP message from
     * @return ServiceURI for the SOAP message that is read from the given {@link XMLStreamReader}
     * @throws SOAPException      if the SOAP has empty body
     * @see org.isolution.nexus.domain.Service
     */
    public Service getServiceURI(XMLStreamReader streamReader) throws SOAPException {
        return getServiceURI(getSOAPEnvelope(streamReader));
    }

    /**
     * @param soapEnvelope {@link SOAPEnvelope}
     * @return ServiceURI for the given {@link SOAPEnvelope}
     * @throws SOAPException  if the SOAP has empty body
     * @see org.isolution.nexus.domain.Service
     */
    public Service getServiceURI(SOAPEnvelope soapEnvelope) throws SOAPException {
        OMNamespace soapBodyFirstElementNS = soapEnvelope.getSOAPBodyFirstElementNS();
        if (soapBodyFirstElementNS == null) {
            throw new SOAPException("Empty SOAP Body");
        }
        String localName = soapEnvelope.getSOAPBodyFirstElementLocalName();
        return new Service(soapBodyFirstElementNS.getNamespaceURI(), localName);
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

    private SOAPEnvelope getSOAPEnvelope(XMLStreamReader streamReader) {
        StAXSOAPModelBuilder soapModelBuilder = new StAXSOAPModelBuilder(streamReader);
        return soapModelBuilder.getSOAPEnvelope();
    }
}