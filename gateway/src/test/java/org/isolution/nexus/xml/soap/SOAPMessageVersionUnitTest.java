package org.isolution.nexus.xml.soap;

import org.apache.axiom.soap.SOAPProcessingException;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * User: agwibowo
 * Date: 27/12/10
 * Time: 10:19 PM
 */
public class SOAPMessageVersionUnitTest {
    private SOAPMessageUtil soapMessageUtil;

    @Before
    public void setup() {
        soapMessageUtil = new SOAPMessageUtil();
    }

    @Test
    public void should_work_with_soap11_string() throws Exception {
        SOAPMessageSuite.Fixture soap11Fixture = SOAPMessageSuite.SOAP11ResponseFixture;
        assertThat(soapMessageUtil.getSOAPVersion(soap11Fixture.getSOAPStr()),
                is(soap11Fixture.getSOAPVersion()));
    }

    @Test
    public void should_work_with_soap12_string() throws Exception {
        SOAPMessageSuite.Fixture soap12Fixture = SOAPMessageSuite.SOAP12Fixture;
        assertThat(soapMessageUtil.getSOAPVersion(soap12Fixture.getSOAPStr()),
                is(soap12Fixture.getSOAPVersion()));
    }

    @Test
    public void should_work_with_SOAP_with_empty_body() throws Exception {
        SOAPMessageSuite.Fixture fixture = SOAPMessageSuite.emptySOAPBodyFixture;
        assertThat(soapMessageUtil.getSOAPVersion(fixture.getSOAPStr()),
                is(fixture.getSOAPVersion()));
    }

    @Test(expected = SOAPProcessingException.class)
    public void should_throw_exception_on_non_soap_message() throws Exception {
        soapMessageUtil.getSOAPVersion(SOAPMessageSuite.nonSOAPMessageWithNamespaceFixture.getSOAPStr());
    }

    @Test
    public void should_work_with_invalid_SOAP11_message() throws Exception {
        SOAPMessageSuite.Fixture fixture = SOAPMessageSuite.invalidSOAP11Fixture;
        assertThat(soapMessageUtil.getSOAPVersion(fixture.getSOAPStr()),
                is(fixture.getSOAPVersion()));
    }

    @Test
    public void should_work_with_non_well_formed_SOAP_message() throws Exception {
        SOAPMessageSuite.Fixture fixture = SOAPMessageSuite.nonWellFormedSOAPMessageFixture;
        assertThat(soapMessageUtil.getSOAPVersion(fixture.getSOAPStr()),
                is(fixture.getSOAPVersion()));
    }

    @Test
    public void should_work_with_non_well_formed_SOAP_message_body_content() throws Exception {
        SOAPMessageSuite.Fixture fixture = SOAPMessageSuite.nonWellFormedSOAPBodyContentFixture;
        assertThat(soapMessageUtil.getSOAPVersion(fixture.getSOAPStr()),
                is(fixture.getSOAPVersion()));
    }

    @Test
    public void should_work_with_invalid_SOAP12_message() throws Exception {
        SOAPMessageSuite.Fixture fixture = SOAPMessageSuite.invalidSOAP12Fixture;
        assertThat(soapMessageUtil.getSOAPVersion(fixture.getSOAPStr()),
                is(fixture.getSOAPVersion()));
    }
}
