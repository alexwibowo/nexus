package org.isolution.nexus.xml.soap;

import org.apache.axiom.om.OMException;
import org.apache.axiom.soap.SOAPProcessingException;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.xml.soap.SOAPException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * User: Alex Wibowo
 * Date: 18/12/10
 * Time: 11:41 PM
 */
public class SOAPMessageUtilUnitTest extends AbstractSOAPMessageUnitTest{

    private SOAPMessageUtil soapMessageUtil;

    @Before
    public void setup() {
        super.setup();
        soapMessageUtil = new SOAPMessageUtil();
    }

    @Test
    public void should_work_with_soap11_string() throws Exception {
        SOAPMessageSuite.Fixture fixture = SOAPMessageSuite.SOAP11ResponseFixture;
        assertThat(soapMessageUtil.getNamespace(fixture.getSOAPStr()),
                is(fixture.getNamespace()));
    }

    @Test
    public void should_work_with_soap11_XMLStreamReader() throws Exception {
        SOAPMessageSuite.Fixture fixture = SOAPMessageSuite.SOAP11ResponseFixture;
        assertThat(soapMessageUtil.getNamespace(getXMLStreamReader(fixture.getSOAPStr())),
                is(fixture.getNamespace()));
    }

    @Test
    public void should_work_with_soap11_SOAPEnvelope() throws Exception {
        SOAPMessageSuite.Fixture fixture = SOAPMessageSuite.SOAP11ResponseFixture;
        assertThat(soapMessageUtil.getNamespace(getSOAPEnvelope(fixture.getSOAPStr())),
                is(fixture.getNamespace()));
    }

    @Test
    public void should_work_with_soap12_string() throws Exception {
        SOAPMessageSuite.Fixture fixture = SOAPMessageSuite.SOAP12Fixture;
        assertThat(soapMessageUtil.getNamespace(fixture.getSOAPStr()),
                is(fixture.getNamespace()));
    }

    @Test
    public void should_work_with_soap12_XMLStreamReader() throws Exception {
        SOAPMessageSuite.Fixture fixture = SOAPMessageSuite.SOAP12Fixture;
        assertThat(soapMessageUtil.getNamespace(getXMLStreamReader(fixture.getSOAPStr())),
                is(fixture.getNamespace()));
    }

    @Test
    public void should_work_with_soap12_SOAPEnvelope() throws Exception {
        SOAPMessageSuite.Fixture fixture = SOAPMessageSuite.SOAP12Fixture;
        assertThat(soapMessageUtil.getNamespace(getSOAPEnvelope(fixture.getSOAPStr())),
                is(fixture.getNamespace()));
    }

    @Test
    public void should_throw_SOAPException_on_soap_with_empty_body() throws Exception {
        try {
            soapMessageUtil.getNamespace(SOAPMessageSuite.emptySOAPBody);
            fail("Should have failed, due to empty SOAP body");
        } catch (SOAPException e) {
            assertThat(e.getMessage(), is("Empty SOAP Body"));
        }
    }

    @Test
    public void should_return_namespace_of_first_child_of_soap11_body() throws Exception {
        assertThat(soapMessageUtil.getNamespace(SOAPMessageSuite.soap11WithMultipleChild), is("http://www.soap11.com"));
    }

    @Test
    public void should_return_namespace_of_first_child_of_soap12_body() throws Exception {
        assertThat(soapMessageUtil.getNamespace(SOAPMessageSuite.soap12WithMultipleChild), is("http://www.soap12.com"));
    }

    @Test
    @Ignore
    public void should_throw_exception_on_non_well_formed_SOAP() throws Exception {
        try {
            soapMessageUtil.getNamespace(SOAPMessageSuite.nonWellFormedSOAPMessage);
            fail("Should have failed, due to non well formed SOAP message");
        } catch (OMException e) {
            assertThat(e.getMessage(), containsString("Unexpected close tag </SOAP-ENV:Envelope>"));
            assertThat(e.getMessage(), containsString("expected </SOAP-ENV:Body>"));
        }
    }

    @Test
    @Ignore
    public void should_throw_exception_on_non_well_formed_SOAPBody_content() throws Exception {
        try {
            soapMessageUtil.getNamespace(SOAPMessageSuite.nonWellFormedSOAPBodyContentMessage);
            fail("Should have failed, due to non well formed SOAP Body content message");
        } catch (OMException e) {
            assertThat(e.getMessage(), containsString("Unexpected close tag </SOAP-ENV:Body>"));
            assertThat(e.getMessage(), containsString("expected </res:result>"));
        }
    }

    @Test(expected = SOAPProcessingException.class)
    public void should_throw_SOAPProcessingException_on_non_soap_message() throws Exception {
        soapMessageUtil.getNamespace(SOAPMessageSuite.nonSOAPMessageWithoutNamespace);
    }

    @Test(expected = SOAPProcessingException.class)
    public void should_throw_SOAPProcessingException_on_non_soap_message_with_namespace() throws Exception {
        soapMessageUtil.getNamespace(SOAPMessageSuite.nonSOAPMessageWithNamespace);
    }

    @Test(expected = SOAPProcessingException.class)
    public void should_throw_exception_on_invalid_SOAP11_message() throws Exception {
        soapMessageUtil.getNamespace(SOAPMessageSuite.invalidSOAP11Message);
    }

    @Test(expected = SOAPProcessingException.class)
    public void should_throw_exception_on_invalid_SOAP12_message() throws Exception {
        soapMessageUtil.getNamespace(SOAPMessageSuite.invalidSOAP12Message);
    }

    @Test
    public void should_throw_InvalidArgumentException_on_empty_string() throws Exception {
        try {
            soapMessageUtil.getNamespace(" ");
            fail("Should have failed, due to empty SOAP message");
        } catch (Exception e) {
            assertThat(e, Matchers.instanceOf(IllegalArgumentException.class));
            assertThat(e.getMessage(), Matchers.is("SOAP message must be supplied"));
        }
    }

    @Test
    public void should_throw_InvalidArgumentException_on_null_soap() throws Exception {
        try {
            soapMessageUtil.getNamespace((String) null);
            fail("Should have failed, due to empty SOAP message");
        } catch (Exception e) {
            assertThat(e, Matchers.instanceOf(IllegalArgumentException.class));
            assertThat(e.getMessage(), Matchers.is("SOAP message must be supplied"));
        }
    }
}
