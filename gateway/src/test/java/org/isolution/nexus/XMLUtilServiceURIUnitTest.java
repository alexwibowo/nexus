package org.isolution.nexus;

import org.isolution.nexus.domain.ServiceURI;
import org.isolution.nexus.xml.AbstractXMLUnitTest;
import org.isolution.nexus.xml.XMLUtil;
import org.junit.Before;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

/**
 * User: agwibowo
 * Date: 28/12/10
 * Time: 12:28 AM
 */
public class XMLUtilServiceURIUnitTest extends AbstractXMLUnitTest {
    private XMLUtil xmlUtil;

    @Before
    public void setup() {
        super.setup();
        xmlUtil = new XMLUtil();
    }

    @Test
    public void should_work_in_simple_case() throws Exception {
        ServiceURI serviceURI = xmlUtil.getServiceURI(getXMLStreamReader("<root xmlns=\"http://www.alex.com\"><title>Alex</title></root>"));
        assertThat(serviceURI.getNamespace(), is("http://www.alex.com"));
        assertThat(serviceURI.getLocalName(), is("root"));
    }

    @Test
    public void should_work_with_complex_document() throws Exception {
        ServiceURI serviceURI = xmlUtil.getServiceURI(getXMLStreamReader("<root xmlns=\"http://www.alex.com\"><person><firstName>Alex</firstName><lastName>Wibowo</lastName</person></root>"));
        assertThat(serviceURI.getNamespace(), is("http://www.alex.com"));
        assertThat(serviceURI.getLocalName(), is("root"));
    }

    @Test
    public void should_fail_if_theres_no_namespace_for_root_element() throws Exception {
        try {
            xmlUtil.getServiceURI(getXMLStreamReader("<root><title>Alex</title></root>"));
            fail("Should have failed - due to document without namespace");
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("Invalid namespace: [null]"));
        }
    }

    @Test
    public void should_return_namespace_of_the_root_element() throws Exception {
        ServiceURI serviceURI = xmlUtil.getServiceURI(getXMLStreamReader("<root xmlns:desc=\"http://www.bogus.com\" xmlns=\"http://www.alex.com\" ><title>Alex</title><desc:description>another chapter</desc:description></root>"));
        assertThat(serviceURI.getNamespace(), is("http://www.alex.com"));
    }

    @Test
    public void should_use_first_child_elementName_as_localName() throws Exception {
        ServiceURI serviceURI = xmlUtil.getServiceURI(getXMLStreamReader("<root xmlns:desc=\"http://www.bogus.com\" xmlns=\"http://www.alex.com\" ><title>Alex</title><desc:description>another chapter</desc:description></root>"));
        assertThat(serviceURI.getLocalName(), is("root"));

    }

    @Test
    public void should_fail_for_empty_XML() throws Exception {
        try {
            xmlUtil.getServiceURI(getXMLStreamReader("   "));
            fail("Should have failed, due to empty XML");
        } catch (Exception e) {
        }
    }

    @Test
    public void should_fail_for_non_XML_message() throws Exception {
        try {
            xmlUtil.getServiceURI(getXMLStreamReader(" abcdef "));
            fail("Should have failed, due to non XML message");
        } catch (Exception e) {
        }
    }

    @Test
    public void should_compensate_for_non_wellFormed_XML_message() throws Exception {
        ServiceURI serviceURI = xmlUtil.getServiceURI(getXMLStreamReader("<root xmlns=\"http://www.alex.com\"><title>Alex"));
        assertThat(serviceURI.getNamespace(), is("http://www.alex.com"));
        assertThat(serviceURI.getLocalName(), is("root"));
    }

    @Test
    public void should_work_for_XML_with_comments() throws Exception {
        ServiceURI serviceURI = xmlUtil.getServiceURI(getXMLStreamReader("<!-- This is a comment --><root xmlns=\"http://www.alex.com\"><title>Alex</title></root>"));
        assertThat(serviceURI.getNamespace(), is("http://www.alex.com"));
        assertThat(serviceURI.getLocalName(), is("root"));
    }
}
