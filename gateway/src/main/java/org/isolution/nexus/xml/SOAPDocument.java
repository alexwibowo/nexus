package org.isolution.nexus.xml;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.isolution.nexus.domain.Service;
import org.isolution.nexus.domain.ServiceURI;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.OutputStream;

/**
 * A representation of a single SOAP message that will be read from a given {@link XMLStreamReader}.
 *
 * User: agwibowo
 * Date: 27/12/10
 * Time: 8:03 PM
 */
public class SOAPDocument {

    private StAXOMBuilder stAXOMBuilder;

    public SOAPDocument(XMLStreamReader reader) {
        stAXOMBuilder = new StAXOMBuilder(reader);
    }

    /**
     *
     * @return {@link org.isolution.nexus.domain.ServiceURI} for the XML document containing the namespace for the root element
     * as the {@link org.isolution.nexus.domain.Service#serviceURI}'s{@link org.isolution.nexus.domain.ServiceURI#namespace}
     * and the root element name as the {@link org.isolution.nexus.domain.Service#serviceURI}'s {@link org.isolution.nexus.domain.ServiceURI#localName}
     */
    public ServiceURI getServiceURI() {
        OMElement documentElement = getDocumentElement();
        OMNamespace omNamespace = documentElement.getNamespace();
        String namespaceURI = omNamespace!=null?omNamespace.getNamespaceURI():null;
        String localName = documentElement.getLocalName();
        return new ServiceURI(namespaceURI, localName);
    }

    private OMElement getDocumentElement() {
        return stAXOMBuilder.getDocumentElement();
    }

    /**
     * @param os {@link OutputStream} to write the SOAP document to
     * @throws XMLStreamException exception that occurred during the XML serialization
     */
    public void writeTo(OutputStream os)
            throws XMLStreamException {
        stAXOMBuilder.getDocument().serialize(os);
    }

    /**
     * @return character encoding of the SOAP document
     */
    public String getCharacterEncoding() {
        return stAXOMBuilder.getCharacterEncoding();
    }
}
