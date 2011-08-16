package org.isolution.nexus.xml.soap;

import org.apache.axiom.soap.SOAPProcessingException;
import org.isolution.nexus.domain.Service;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.xml.soap.SOAPException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;

/**
 * User: agwibowo
 * Date: 24/12/10
 * Time: 6:46 PM
 */
public class SOAPMessageToServiceURIUnitTest extends AbstractSOAPMessageUnitTest{

    private SOAPMessageUtil soapMessageUtil;

    @Before
    public void setup() {
        super.setup();
        soapMessageUtil = new SOAPMessageUtil();
    }


    @Test
    public void should_work_with_soap11_string() throws Exception {
        SOAPMessageSuite.Fixture fixture = SOAPMessageSuite.SOAP11ResponseFixture;
        Service service = soapMessageUtil.getServiceURI(fixture.getSOAPStr());
        assertThat(service, not(nullValue()));
        assertThat(service.getServiceURI().getNamespace(), is(fixture.getNamespace()));
        assertThat(service.getServiceURI().getLocalName(), is(fixture.getLocalName()));
    }

    @Test
    public void should_work_with_soap11_XMLStreamReader() throws Exception {
        SOAPMessageSuite.Fixture fixture = SOAPMessageSuite.SOAP11ResponseFixture;
        Service service = soapMessageUtil.getServiceURI(getXMLStreamReader(fixture.getSOAPStr()));
        assertThat(service, not(nullValue()));
        assertThat(service.getServiceURI().getNamespace(), is(fixture.getNamespace()));
        assertThat(service.getServiceURI().getLocalName(), is(fixture.getLocalName()));
    }

    @Test
    public void should_work_with_soap11_SOAPEnvelope() throws Exception {
        SOAPMessageSuite.Fixture fixture = SOAPMessageSuite.SOAP11ResponseFixture;
        Service service = soapMessageUtil.getServiceURI(getSOAPEnvelope(fixture.getSOAPStr()));
        assertThat(service, not(nullValue()));
        assertThat(service.getServiceURI().getNamespace(), is(fixture.getNamespace()));
        assertThat(service.getServiceURI().getLocalName(), is(fixture.getLocalName()));

    }

    @Test
    public void should_work_with_soap11_with_multiple_body() throws Exception {
        SOAPMessageSuite.Fixture fixture = SOAPMessageSuite.SOAP11WithMultipleChildFixture;
        Service service = soapMessageUtil.getServiceURI(fixture.getSOAPStr());
        assertThat(service, not(nullValue()));
        assertThat(service.getServiceURI().getNamespace(), is(fixture.getNamespace()));
        assertThat(service.getServiceURI().getLocalName(), is(fixture.getLocalName()));
    }

    @Test
    public void should_work_with_soap12_string() throws Exception {
        SOAPMessageSuite.Fixture fixture = SOAPMessageSuite.SOAP12Fixture;
        Service service = soapMessageUtil.getServiceURI(fixture.getSOAPStr());
        assertThat(service.getServiceURI().getNamespace(), is(fixture.getNamespace()));
        assertThat(service.getServiceURI().getLocalName(), is(fixture.getLocalName()));
    }

    @Test
    public void should_work_with_soap12_XMLStreamReader() throws Exception {
        SOAPMessageSuite.Fixture fixture = SOAPMessageSuite.SOAP12Fixture;
        Service service = soapMessageUtil.getServiceURI(getXMLStreamReader(fixture.getSOAPStr()));
        assertThat(service.getServiceURI().getNamespace(), is(fixture.getNamespace()));
        assertThat(service.getServiceURI().getLocalName(), is(fixture.getLocalName()));
    }

    @Test
    public void should_work_with_soap12_SOAPEnvelope() throws Exception {
        SOAPMessageSuite.Fixture fixture = SOAPMessageSuite.SOAP12Fixture;
        Service service = soapMessageUtil.getServiceURI(getSOAPEnvelope(fixture.getSOAPStr()));
        assertThat(service.getServiceURI().getNamespace(), is(fixture.getNamespace()));
        assertThat(service.getServiceURI().getLocalName(), is(fixture.getLocalName()));
    }

    @Test
    public void should_work_with_soap12_string_with_multiple_body() throws Exception {
        SOAPMessageSuite.Fixture fixture = SOAPMessageSuite.SOAP12WithMultipleChildFixture;
        Service service = soapMessageUtil.getServiceURI(fixture.getSOAPStr());
        assertThat(service, not(nullValue()));
        assertThat(service.getServiceURI().getNamespace(), is(fixture.getNamespace()));
        assertThat(service.getServiceURI().getLocalName(), is(fixture.getLocalName()));
    }

    @Test
    public void should_fail_for_empty_soapMessage() throws Exception{
        SOAPMessageSuite.Fixture fixture = SOAPMessageSuite.emptySOAPBodyFixture;
        try {
            soapMessageUtil.getServiceURI(fixture.getSOAPStr());
            fail("Should have failed due to empty SOAP message");
        } catch (SOAPException e) {
            assertThat(e.getMessage(), is("Empty SOAP Body"));
        }
    }

    @Test
    public void should_fail_for_invalid_SOAP11() throws Exception {
        SOAPMessageSuite.Fixture fixture = SOAPMessageSuite.invalidSOAP11Fixture;
        try {
            soapMessageUtil.getServiceURI(fixture.getSOAPStr());
            fail("Should have failed, due to invalid message");
        } catch (SOAPProcessingException e) {
            assertThat(e.getMessage(), containsString("Body2"));
        }
    }

    @Test
    public void should_fail_for_invalid_SOAP12() throws Exception {
        SOAPMessageSuite.Fixture fixture = SOAPMessageSuite.invalidSOAP12Fixture;
        try {
            soapMessageUtil.getServiceURI(fixture.getSOAPStr());
            fail("Should have failed, due to invalid message");
        } catch (SOAPProcessingException e) {
            assertThat(e.getMessage(), containsString("Body2"));
        }
    }

    @Test
    @Ignore
    public void should_fail_for_nonWellFormed_SOAP() throws Exception {
        SOAPMessageSuite.Fixture fixture = SOAPMessageSuite.nonWellFormedSOAPMessageFixture;
        try {
            soapMessageUtil.getServiceURI(fixture.getSOAPStr());
            fail("Should have failed, due to non well formed message");
        } catch (SOAPProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void should_fail_for_non_SOAP_without_namespace() throws Exception {
        SOAPMessageSuite.Fixture fixture = SOAPMessageSuite.nonSOAPMessageWithoutNamespaceFixture;
        try {
            soapMessageUtil.getServiceURI(fixture.getSOAPStr());
            fail("Should have failed, due to non SOAP message");
        } catch (SOAPProcessingException e) {
        }
    }

    @Test
    public void should_fail_for_non_SOAP_with_namespace() throws Exception{
        SOAPMessageSuite.Fixture fixture = SOAPMessageSuite.nonSOAPMessageWithNamespaceFixture;
        try {
            soapMessageUtil.getServiceURI(fixture.getSOAPStr());
            fail("Should have failed, due to non SOAP message");
        } catch (SOAPProcessingException e) {
        }

    }
}