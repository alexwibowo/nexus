package org.isolution.nexus.xml.soap;

import org.apache.axiom.soap.SOAP11Version;
import org.apache.axiom.soap.SOAP12Version;
import org.apache.axiom.soap.SOAPProcessingException;
import org.apache.commons.lang.NotImplementedException;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import javax.xml.soap.SOAPException;
import javax.xml.stream.XMLStreamException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * User: agwibowo
 * Date: 18/12/10
 * Time: 11:41 PM
 */
public class SOAPMessageUtilUnitTest {

    private SOAPMessageUtil soapMessageUtil;

    private static final String emptySOAPBody = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\""
            + "     xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
            + "     xmlns:res=\"http://www.alex-wibowo.com\" "
            + "     xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">"
            + "     <SOAP-ENV:Body></SOAP-ENV:Body>"
            + "</SOAP-ENV:Envelope>";

    private static final String soap11Message = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"" +
            "       SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"" +
            "       xmlns:res=\"http://www.soap11.com\"" +
            "       xmlns:xsi=\"http://www.w3.org/1999/XMLSchema-instance\"" +
            "       xmlns:xsd=\"http://www.w3.org/1999/XMLSchema\">" +
            "           <SOAP-ENV:Body>\n" +
            "               <res:GetLastTradePriceResponse>\n" +
            "                   <res:Price>34.5</res:Price>\n" +
            "               </res:GetLastTradePriceResponse>\n" +
            "           </SOAP-ENV:Body>\n" +
            "       </SOAP-ENV:Envelope>";

    private static final String soap12Message = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\" "
            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
            + "xmlns:res=\"http://www.soap12.com\" "
            + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">"
            + "<SOAP-ENV:Body>"
            + "<res:result>"
            + "<message xsi:type=\"xsd:string\">Hello World</message>"
            + "</res:result>"
            + "</SOAP-ENV:Body>"
            + "</SOAP-ENV:Envelope>";

    private static final String soap12WithMultipleChild = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\" "
            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
            + "xmlns:res=\"http://www.soap12.com\" "
            + "xmlns:res2=\"http://www.soap12-2.com\" "
            + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">"
            + "     <SOAP-ENV:Body>"
            + "         <res:result>"
            + "             <message xsi:type=\"xsd:string\">Hello World</message>"
            + "         </res:result>"
            + "         <res2:result2>"
            + "             <message xsi:type=\"xsd:string\">Hello World</message>"
            + "         </res2:result2>"
            + "     </SOAP-ENV:Body>"
            + "</SOAP-ENV:Envelope>";


    private static final String soap11WithMultipleChild = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"" +
            "       SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"" +
            "       xmlns:res=\"http://www.soap11.com\" " +
            "       xmlns:res2=\"http://www.soap11-2.com\" " +
            "       xmlns:xsi=\"http://www.w3.org/1999/XMLSchema-instance\"" +
            "       xmlns:xsd=\"http://www.w3.org/1999/XMLSchema\">" +
            "           <SOAP-ENV:Body>\n" +
            "               <res:GetLastTradePriceResponse>\n" +
            "                   <res:Price>34.5</res:Price>\n" +
            "               </res:GetLastTradePriceResponse>\n" +
            "               <res2:GetLastTradePriceResponse>\n" +
            "                   <res2:Price>34.5</res2:Price>\n" +
            "               </res2:GetLastTradePriceResponse>\n" +
            "           </SOAP-ENV:Body>\n" +
            "       </SOAP-ENV:Envelope>";

    private static final String invalidSOAP11Message = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"" +
            "       SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"" +
            "       xmlns:res=\"http://www.soap11.com\"" +
            "       xmlns:xsi=\"http://www.w3.org/1999/XMLSchema-instance\"" +
            "       xmlns:xsd=\"http://www.w3.org/1999/XMLSchema\">" +
            "           <SOAP-ENV:Body2>\n" +
            "               <res:GetLastTradePriceResponse>\n" +
            "                   <res:Price>34.5</res:Price>\n" +
            "               </res:GetLastTradePriceResponse>\n" +
            "           </SOAP-ENV:Body2>\n" +
            "       </SOAP-ENV:Envelope>";

    private static final String invalidSOAP12Message = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\" "
            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
            + "xmlns:res=\"http://www.soap12.com\" "
            + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">"
            + "<SOAP-ENV:Body2>"
            + "<res:result>"
            + "<message xsi:type=\"xsd:string\">Hello World</message>"
            + "</res:result>"
            + "</SOAP-ENV:Body2>"
            + "</SOAP-ENV:Envelope>";

    private static final String nonWellFormedSOAPMessage = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\" "
            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
            + "xmlns:res=\"http://www.soap12.com\" "
            + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">"
            + "<SOAP-ENV:Body2>"
            + "<res:result>"
            + "<message xsi:type=\"xsd:string\">Hello World</message>"
            + "</res:result>"
            + "</SOAP-ENV:Envelope>";


    private static final String nonSOAPMessageWithoutNamespace = "<message>HelloWorld</message>";

    private static final String nonSOAPMessageWithNamespace = "<message xmlns=\"http://www.alex-wibowo.com\">HelloWorld</message>";

    @Before
    public void setup() {
        soapMessageUtil = new SOAPMessageUtil();
    }

    @Test
    public void should_work_with_soap11() throws Exception {
        assertThat(soapMessageUtil.getSOAPVersion(soap11Message), is(SOAP11Version.class));
        assertThat(soapMessageUtil.getNamespace(soap11Message), is("http://www.soap11.com"));
    }

    @Test
    public void should_work_with_soap12() throws Exception {
        assertThat(soapMessageUtil.getSOAPVersion(soap12Message), is(SOAP12Version.class));
        assertThat(soapMessageUtil.getNamespace(soap12Message), is("http://www.soap12.com"));
    }

    @Test
    public void should_return_namespace_of_child_of_soap_body() throws Exception {
        assertThat(soapMessageUtil.getNamespace(soap11Message), is("http://www.soap11.com"));
        assertThat(soapMessageUtil.getNamespace(soap12Message), is("http://www.soap12.com"));
    }

    @Test
    public void should_throw_SOAPException_on_soap_with_empty_body() throws Exception {
        try {
            soapMessageUtil.getNamespace(emptySOAPBody);
            fail("Should have failed, due to empty SOAP body");
        } catch (SOAPException e) {
            assertThat(e.getMessage(), is("Empty SOAP Body"));
        }
    }

    @Test
    public void should_return_namespace_of_first_child_of_soap11_body() throws Exception {
        assertThat(soapMessageUtil.getNamespace(soap11WithMultipleChild), is("http://www.soap11.com"));
    }
    @Test
    public void should_return_namespace_of_first_child_of_soap12_body() throws Exception {
        assertThat(soapMessageUtil.getNamespace(soap12WithMultipleChild), is("http://www.soap12.com"));
    }

    @Test(expected = SOAPProcessingException.class)
    public void should_throw_exception_on_non_well_formed_SOAP() throws Exception {
        soapMessageUtil.getNamespace(nonWellFormedSOAPMessage);
    }

    @Test(expected = SOAPProcessingException.class)
    public void should_throw_SOAPProcessingException_on_non_soap_message() throws Exception {
        soapMessageUtil.getNamespace(nonSOAPMessageWithoutNamespace);
    }

    @Test(expected = SOAPProcessingException.class)
    public void should_throw_SOAPProcessingException_on_non_soap_message_with_namespace() throws Exception {
        soapMessageUtil.getNamespace(nonSOAPMessageWithNamespace);
    }

    @Test(expected = SOAPProcessingException.class)
    public void should_throw_exception_on_invalid_SOAP11_message() throws Exception {
        soapMessageUtil.getNamespace(invalidSOAP11Message);
    }

    @Test(expected = SOAPProcessingException.class)
    public void should_throw_exception_on_invalid_SOAP12_message() throws Exception {
        soapMessageUtil.getNamespace(invalidSOAP12Message);
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
            soapMessageUtil.getNamespace(null);
            fail("Should have failed, due to empty SOAP message");
        } catch (Exception e) {
            assertThat(e, Matchers.instanceOf(IllegalArgumentException.class));
            assertThat(e.getMessage(), Matchers.is("SOAP message must be supplied"));
        }

    }


}
