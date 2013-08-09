package org.isolution.xml.soap;

import org.isolution.nexus.Employee;
import org.isolution.nexus.Holiday;
import org.isolution.nexus.HolidayRequest;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.GregorianCalendar;

import static org.hamcrest.Matchers.containsString;
import static org.isolution.xml.matchers.XMLGregorianCalendarMatchers.matchesIgnoringTime;
import static org.junit.Assert.assertThat;

public class SOAPMessageBuilderUnitTest {

    private JAXBContext jaxbContext;

    @Before
    public void setup() throws JAXBException {
        jaxbContext = JAXBContext.newInstance(HolidayRequest.class);
    }

    @Test
    public void should_create_soap12_by_default() throws Exception {
        HolidayRequest holidayRequest = createHolidayRequest();

        SOAPMessage soapMessage = SOAPMessageBuilder.getDefault(jaxbContext, holidayRequest).build();
        String soapString = transformToString(soapMessage);
        assertThat(soapString, containsString(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE));
    }

    @Test
    public void should_be_able_to_create_soap11() throws Exception{
        HolidayRequest holidayRequest = createHolidayRequest();

        SOAPMessage soapMessage = SOAPMessageBuilder.getDefault(jaxbContext, holidayRequest).asSoap11().build();
        String soapString = transformToString(soapMessage);
        assertThat(soapString, containsString(SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE));
    }

    @Test
    public void should_create_soap12_correctly() throws Exception {
        HolidayRequest holidayRequest = createHolidayRequest();

        SOAPMessage soapMessage = SOAPMessageBuilder.getDefault(jaxbContext, holidayRequest).build();
        SOAPMessage reconstructedSoapMessage = SOAPMessageBuilder.fromSOAPString(transformToString(soapMessage));

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        HolidayRequest reconstructedHolidayRequest = (HolidayRequest) unmarshaller.unmarshal(reconstructedSoapMessage.getSOAPBody().getFirstChild());
        expectRequestToBeTheSame(holidayRequest, reconstructedHolidayRequest);
    }

    @Test
    public void should_create_soap11_correctly() throws Exception {
        HolidayRequest holidayRequest = createHolidayRequest();

        SOAPMessage soapMessage = SOAPMessageBuilder.getDefault(jaxbContext, holidayRequest).asSoap11().build();
        SOAPMessage reconstructedSoapMessage = SOAPMessageBuilder.fromSOAPString(transformToString(soapMessage));

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        HolidayRequest reconstructedHolidayRequest = (HolidayRequest) unmarshaller.unmarshal(reconstructedSoapMessage.getSOAPBody().getFirstChild());
        expectRequestToBeTheSame(holidayRequest, reconstructedHolidayRequest);
    }


    private void expectRequestToBeTheSame(HolidayRequest holidayRequest, HolidayRequest reconstructedHolidayRequest) {
        assertThat(reconstructedHolidayRequest.getHoliday().getStartDate(), matchesIgnoringTime(holidayRequest.getHoliday().getStartDate()));
        assertThat(reconstructedHolidayRequest.getHoliday().getEndDate(), matchesIgnoringTime(holidayRequest.getHoliday().getEndDate()));
    }

    private String transformToString(SOAPMessage soapMessage) throws SOAPException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        soapMessage.writeTo(baos);
        return new String(baos.toByteArray(), "UTF-8").replaceAll(">\\s*<", "><"); // get rid of whitespaces for easier testing
    }

    private HolidayRequest createHolidayRequest() throws DatatypeConfigurationException {
        Holiday holiday = new Holiday();

        GregorianCalendar startCalendar = new GregorianCalendar();
        startCalendar.setTime(new DateTime().plusDays(5).toDate());
        XMLGregorianCalendar startDateHoliday = DatatypeFactory.newInstance().newXMLGregorianCalendar(startCalendar);
        holiday.setStartDate(startDateHoliday);

        GregorianCalendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(new DateTime().plusDays(10).toDate());
        XMLGregorianCalendar endDateHoliday = DatatypeFactory.newInstance().newXMLGregorianCalendar(endCalendar);
        holiday.setEndDate(endDateHoliday);

        HolidayRequest holidayRequest = new HolidayRequest();
        holidayRequest.setHoliday(holiday);

        Employee employee = new Employee();
        employee.setFirstName("Alex");
        employee.setLastName("Wibowo");
        holidayRequest.setEmployee(employee);
        return holidayRequest;
    }
}
