package org.isolution.nexus;

import org.springframework.core.io.InputStreamResource;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.xml.transform.ResourceSource;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.GregorianCalendar;

public class Test {

    public static void main(String[] args) throws JAXBException, DatatypeConfigurationException, SOAPException, ParserConfigurationException, IOException, SAXException {
//        testSoapCreation();
        testSchemaValidation();
    }

    private static void testSchemaValidation() throws IOException, SAXException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        ResourceSource resourceSource = new ResourceSource(new InputStreamResource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xsd/company.xsd")));

        Schema schema = schemaFactory.newSchema(resourceSource);
        Validator validator = schema.newValidator();
        validator.setErrorHandler(new ErrorHandler() {
            @Override
            public void warning(SAXParseException exception) throws SAXException {
                System.out.println("Warning!");
            }

            @Override
            public void error(SAXParseException exception) throws SAXException {
                System.out.println("Error!");
            }

            @Override
            public void fatalError(SAXParseException exception) throws SAXException {
                System.out.println("Fatal Error!");
            }
        });

        ResourceSource xmlResource = new ResourceSource(new InputStreamResource(Thread.currentThread().getContextClassLoader().getResourceAsStream("xml/invalidBudgetHoliday.xml")));
        validator.validate(xmlResource);

    }

    private static void testSoapCreation() throws DatatypeConfigurationException, JAXBException, ParserConfigurationException, SAXException, IOException, SOAPException {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);

        Holiday holiday = new Holiday();
        holiday.setEndDate(xmlGregorianCalendar);

        HolidayRequest holidayRequest = new HolidayRequest();
        holidayRequest.setHoliday(holiday);

        StringWriter stringWriter = new StringWriter();
        JAXBContext jaxbContext = JAXBContext.newInstance(HolidayRequest.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty("jaxb.formatted.output", true);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        marshaller.marshal(holidayRequest, stringWriter);


        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document parse = builder.parse(new ByteArrayInputStream(stringWriter.toString().getBytes()));


        MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        SOAPMessage message = messageFactory.createMessage();
        SOAPBody soapBody = message.getSOAPBody();
        soapBody.addDocument(parse);

        message.writeTo(System.out);


        System.out.println("\n==============================");
        SaajSoapMessageFactory saajSoapMessageFactory = new SaajSoapMessageFactory();
        saajSoapMessageFactory.setSoapVersion(SoapVersion.SOAP_12);
        saajSoapMessageFactory.afterPropertiesSet();
        SaajSoapMessage webServiceMessage = saajSoapMessageFactory.createWebServiceMessage();
        webServiceMessage.setSaajMessage(message);


        webServiceMessage.writeTo(System.out);
    }
}
