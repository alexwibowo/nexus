package org.isolution.nexus.xml;

import org.apache.commons.lang.CharEncoding;
import org.apache.geronimo.mail.util.StringBufferOutputStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ws.soap.SoapMessage;

import javax.xml.stream.XMLStreamException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * User: Alex Wibowo
 * Date: 8/03/11
 * Time: 10:52 PM
 */
public class SOAPDocumentSerializationUnitTest extends AbstractXMLUnitTest {

    @Test
    public void should_write_the_exact_xml_document() throws Exception {
        SOAPDocument soapDocument = new SOAPDocument(mockSoapMessage, getXMLStreamReader("<root xmlns=\"http://www.alex.com\"><title>Alex</title></root>"));
        String actualMessage = getMessageString(soapDocument);
        assertThat(actualMessage, is("<?xml version='1.0' encoding='UTF-8'?><root xmlns=\"http://www.alex.com\"><title>Alex</title></root>"));
    }

    @Test
    public void should_not_discard_comments_in_xml_document() throws Exception {
        SOAPDocument soapDocument = new SOAPDocument(mockSoapMessage, getXMLStreamReader("<!-- This is a comment --><root xmlns=\"http://www.alex.com\"><title>Alex</title></root>"));
        String actualMessage = getMessageString(soapDocument);
        assertThat(actualMessage, is("<?xml version='1.0' encoding='UTF-8'?><!-- This is a comment --><root xmlns=\"http://www.alex.com\"><title>Alex</title></root>"));
    }

    @Test
    public void should_use_specified_character_encoding() throws Exception {
        SOAPDocument soapDocument = new SOAPDocument(mockSoapMessage, getXMLStreamReader("<root xmlns=\"http://www.alex.com\"><title>Alex</title></root>",
                CharEncoding.UTF_16));
        String soapDocumentEncoding = soapDocument.getCharacterEncoding();

        String actualMessage = getMessageString(soapDocument);
        assertThat(actualMessage, is("<?xml version='1.0' encoding='" + soapDocumentEncoding + "'?><root xmlns=\"http://www.alex.com\"><title>Alex</title></root>"));
    }

    private String getMessageString(SOAPDocument soapDocument) throws XMLStreamException {
        StringBuffer buff = new StringBuffer();
        soapDocument.writePayloadTo(new StringBufferOutputStream(buff));
        return buff.toString();
    }
}
