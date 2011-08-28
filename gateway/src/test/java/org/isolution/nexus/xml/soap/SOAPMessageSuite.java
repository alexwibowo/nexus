package org.isolution.nexus.xml.soap;

import org.apache.axiom.soap.SOAP11Version;
import org.apache.axiom.soap.SOAP12Version;
import org.apache.axiom.soap.SOAPVersion;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suite for operations around SOAP Messages
 * <p/>
 * User: Alex Wibowo
 * Date: 24/12/10
 * Time: 3:46 PM
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        SOAPMessageUtilUnitTest.class,
        SOAPMessageToServiceURIUnitTest.class,
        SOAPMessageVersionUnitTest.class
})
public class SOAPMessageSuite {

    public static class Fixture {
        String soapStr;
        String namespace;
        String localName;
        SOAPVersion soapVersion;

        Fixture(String soapStr, String namespace, String localName, SOAPVersion version) {
            this.soapStr = soapStr;
            this.namespace = namespace;
            this.localName = localName;
            this.soapVersion = version;
        }

        public String getSOAPStr() {
            return soapStr;
        }

        public String getNamespace() {
            return namespace;
        }

        public String getLocalName() {
            return localName;
        }

        public SOAPVersion getSOAPVersion() {
            return soapVersion;
        }
    }

    public static final String emptySOAPBody = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\""
            + "     xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
            + "     xmlns:res=\"http://www.alex-wibowo.com\" "
            + "     xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">"
            + "     <SOAP-ENV:Body></SOAP-ENV:Body>"
            + "</SOAP-ENV:Envelope>";
    public static final Fixture emptySOAPBodyFixture = new Fixture(emptySOAPBody, null, null, SOAP12Version.getSingleton());


    public static final String soap11RequestMessage = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"" +
            "       SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"" +
            "       xmlns:res=\"http://www.soap11.com\"" +
            "       xmlns:xsi=\"http://www.w3.org/1999/XMLSchema-instance\"" +
            "       xmlns:xsd=\"http://www.w3.org/1999/XMLSchema\">" +
            "           <SOAP-ENV:Body>\n" +
            "               <res:GetLastTradePriceRequest/>\n" +
            "           </SOAP-ENV:Body>\n" +
            "       </SOAP-ENV:Envelope>";
    public  static final Fixture SOAP11RequestFixture = new Fixture(soap11RequestMessage, "http://www.soap11.com", "GetLastTradePriceRequest", SOAP11Version.getSingleton());

    public static void main(String[] args) {
        System.out.println(soap11RequestMessage);
    }


    public static final String soap11ResponseMessage = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"" +
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
    public  static final Fixture SOAP11ResponseFixture = new Fixture(soap11ResponseMessage, "http://www.soap11.com", "GetLastTradePriceResponse", SOAP11Version.getSingleton());

    public static final String soap12Message = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\" "
            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
            + "xmlns:res=\"http://www.soap12.com\" "
            + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">"
            + "<SOAP-ENV:Body>"
            + "<res:result>"
            + "<message xsi:type=\"xsd:string\">Hello World</message>"
            + "</res:result>"
            + "</SOAP-ENV:Body>"
            + "</SOAP-ENV:Envelope>";
    public static final Fixture SOAP12Fixture = new Fixture(soap12Message, "http://www.soap12.com", "result", SOAP12Version.getSingleton());

    public static final String soap12WithMultipleChild = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\" "
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
    public static final Fixture SOAP12WithMultipleChildFixture = new Fixture(soap12WithMultipleChild, "http://www.soap12.com", "result", SOAP12Version.getSingleton());


    public static final String soap11WithMultipleChild = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"" +
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
    public static final Fixture SOAP11WithMultipleChildFixture = new Fixture(soap11WithMultipleChild, "http://www.soap11.com", "GetLastTradePriceResponse", SOAP11Version.getSingleton());

    public static final String invalidSOAP11Message = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"" +
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
    public static final Fixture invalidSOAP11Fixture = new Fixture(invalidSOAP11Message, null, null, SOAP11Version.getSingleton());



    public static final String invalidSOAP12Message = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\" "
            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
            + "xmlns:res=\"http://www.soap12.com\" "
            + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">"
            + "<SOAP-ENV:Body2>"
            + "<res:result>"
            + "<message xsi:type=\"xsd:string\">Hello World</message>"
            + "</res:result>"
            + "</SOAP-ENV:Body2>"
            + "</SOAP-ENV:Envelope>";
    public static final Fixture invalidSOAP12Fixture = new Fixture(invalidSOAP12Message, null, null,SOAP12Version.getSingleton());

    public static final String nonWellFormedSOAPMessage = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\" "
            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
            + "xmlns:res=\"http://www.soap12.com\" "
            + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">"
            + "<SOAP-ENV:Body>"
            + "<res:result>"
            + "<message xsi:type=\"xsd:string\">Hello World</message>"
            + "</res:result>"
            + "</SOAP-ENV:Envelope>";
    public static final Fixture nonWellFormedSOAPMessageFixture = new Fixture(nonWellFormedSOAPMessage, null, null, SOAP12Version.getSingleton());

    public static final String nonWellFormedSOAPBodyContentMessage =   "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\" "
            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
            + "xmlns:res=\"http://www.soap12.com\" "
            + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">"
            + "<SOAP-ENV:Body>"
            + "<res:result>"
            + "<message xsi:type=\"xsd:string\">Hello World</message>"
            + "</SOAP-ENV:Body>"
            + "</SOAP-ENV:Envelope>";
    public static final Fixture nonWellFormedSOAPBodyContentFixture = new Fixture(nonWellFormedSOAPBodyContentMessage, null, null,SOAP12Version.getSingleton());


    public static final String nonSOAPMessageWithoutNamespace = "<message>HelloWorld</message>";
    public static final Fixture nonSOAPMessageWithoutNamespaceFixture = new Fixture(nonSOAPMessageWithoutNamespace, null, null, null);

    public static final String nonSOAPMessageWithNamespace = "<message xmlns=\"http://www.alex-wibowo.com\">HelloWorld</message>";
    public static final Fixture nonSOAPMessageWithNamespaceFixture = new Fixture(nonSOAPMessageWithNamespace, null, null, null);

}
