package org.isolution.nexus.xml.soap;

import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.impl.builder.StAXSOAPModelBuilder;
import org.isolution.nexus.xml.AbstractXMLUnitTest;

import javax.xml.stream.XMLStreamException;

/**
 * User: Alex Wibowo
 * Date: 28/12/10
 * Time: 12:09 AM
 */
public abstract class AbstractSOAPMessageUnitTest extends AbstractXMLUnitTest{

    protected SOAPEnvelope getSOAPEnvelope(String soapString) throws XMLStreamException {
        return new StAXSOAPModelBuilder(getXMLStreamReader(soapString)).getSOAPEnvelope();
    }

}
