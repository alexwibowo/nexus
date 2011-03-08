package org.isolution.nexus.xml;

import org.isolution.nexus.domain.ServiceURI;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

/**
 * User: agwibowo
 * Date: 28/12/10
 * Time: 12:28 AM
 */
public class SOAPDocumentServiceURIUnitTest extends AbstractXMLUnitTest {

    @Test
    public void should_work_in_simple_case() throws Exception {
        SOAPDocument soapDocument = new SOAPDocument(getXMLStreamReader("<root xmlns=\"http://www.alex.com\"><title>Alex</title></root>"));
        ServiceURI serviceURI = soapDocument.getServiceURI();
        assertThat(serviceURI.getNamespace(), is("http://www.alex.com"));
        assertThat(serviceURI.getLocalName(), is("root"));
    }

    @Test
    public void should_work_with_complex_document() throws Exception {
        SOAPDocument soapDocument = new SOAPDocument(getXMLStreamReader("<root xmlns=\"http://www.alex.com\"><person><firstName>Alex</firstName><lastName>Wibowo</lastName</person></root>"));
        ServiceURI serviceURI = soapDocument.getServiceURI();
        assertThat(serviceURI.getNamespace(), is("http://www.alex.com"));
        assertThat(serviceURI.getLocalName(), is("root"));
    }

    @Test
    public void should_fail_if_theres_no_namespace_for_root_element() throws Exception {
        try {
            SOAPDocument soapDocument = new SOAPDocument(getXMLStreamReader("<root><title>Alex</title></root>"));
            soapDocument.getServiceURI();
            fail("Should have failed - due to document without namespace");
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("Invalid namespace: [null]"));
        }
    }

    @Test
    public void should_return_namespace_of_the_root_element() throws Exception {
        SOAPDocument soapDocument = new SOAPDocument(getXMLStreamReader("<root xmlns:desc=\"http://www.bogus.com\" xmlns=\"http://www.alex.com\" ><title>Alex</title><desc:description>another chapter</desc:description></root>"));
        ServiceURI serviceURI = soapDocument.getServiceURI();
        assertThat(serviceURI.getNamespace(), is("http://www.alex.com"));
    }

    @Test
    public void should_use_first_child_elementName_as_localName() throws Exception {
        SOAPDocument soapDocument = new SOAPDocument(getXMLStreamReader("<root xmlns:desc=\"http://www.bogus.com\" xmlns=\"http://www.alex.com\" ><title>Alex</title><desc:description>another chapter</desc:description></root>"));
        ServiceURI serviceURI = soapDocument.getServiceURI();
        assertThat(serviceURI.getLocalName(), is("root"));

    }

    @Test
    public void should_fail_for_empty_XML() throws Exception {
        try {
            new SOAPDocument(getXMLStreamReader("   ")).getServiceURI();
            fail("Should have failed, due to empty XML");
        } catch (Exception e) {
        }
    }

    @Test
    public void should_fail_for_non_XML_message() throws Exception {
        try {
            new SOAPDocument(getXMLStreamReader(" abcdef ")).getServiceURI();
            fail("Should have failed, due to non XML message");
        } catch (Exception e) {
        }
    }

    @Test
    public void should_compensate_for_non_wellFormed_XML_message() throws Exception {
        SOAPDocument soapDocument = new SOAPDocument(getXMLStreamReader("<root xmlns=\"http://www.alex.com\"><title>Alex"));
        ServiceURI serviceURI = soapDocument.getServiceURI();
        assertThat(serviceURI.getNamespace(), is("http://www.alex.com"));
        assertThat(serviceURI.getLocalName(), is("root"));
    }

    @Test
    public void should_work_for_XML_with_comments() throws Exception {
        SOAPDocument soapDocument = new SOAPDocument(getXMLStreamReader("<!-- This is a comment --><root xmlns=\"http://www.alex.com\"><title>Alex</title></root>"));
        ServiceURI serviceURI = soapDocument.getServiceURI();
        assertThat(serviceURI.getNamespace(), is("http://www.alex.com"));
        assertThat(serviceURI.getLocalName(), is("root"));
    }
}
