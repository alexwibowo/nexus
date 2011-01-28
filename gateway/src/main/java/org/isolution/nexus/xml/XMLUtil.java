package org.isolution.nexus.xml;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.isolution.nexus.domain.Service;
import org.isolution.nexus.domain.ServiceURI;

import javax.xml.stream.XMLStreamReader;

/**
 * User: agwibowo
 * Date: 27/12/10
 * Time: 8:03 PM
 */
public class XMLUtil {

    /**
     *
     * @param reader {@link javax.xml.stream.XMLStreamReader} to read XML document from
     * @return {@link org.isolution.nexus.domain.Service} for the XML document containing the namespace for the root element
     * as the {@link org.isolution.nexus.domain.Service#serviceURI}'s{@link org.isolution.nexus.domain.ServiceURI#namespace}
     * and the root element name as the {@link org.isolution.nexus.domain.Service#serviceURI}'s {@link org.isolution.nexus.domain.ServiceURI#localName}
     */
    public ServiceURI getServiceURI(XMLStreamReader reader) {
        OMElement documentElement = getDocumentElement(reader);
        OMNamespace omNamespace = documentElement.getNamespace();
        String namespaceURI = omNamespace!=null?omNamespace.getNamespaceURI():null;
        String localName = documentElement.getLocalName();
        return new ServiceURI(namespaceURI, localName);
    }

    private OMElement getDocumentElement(XMLStreamReader reader) {
        StAXOMBuilder stAXOMBuilder = new StAXOMBuilder(reader);
        return stAXOMBuilder.getDocumentElement();
    }

    public String serviceURI(String namespace, String localname) {
        return String.format("%s:%s", namespace, localname);
    }
}
