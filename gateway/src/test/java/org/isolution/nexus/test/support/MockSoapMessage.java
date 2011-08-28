package org.isolution.nexus.test.support;

import org.springframework.ws.mime.Attachment;
import org.springframework.ws.mime.AttachmentException;
import org.springframework.ws.soap.AbstractSoapMessage;
import org.springframework.ws.soap.SoapEnvelope;
import org.springframework.ws.soap.SoapEnvelopeException;
import org.springframework.ws.soap.SoapMessage;
import org.w3c.dom.Document;

import javax.activation.DataHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

/**
 * User: Alex Wibowo
 * Date: 28/08/11
 * Time: 4:51 PM
 */
public class MockSoapMessage extends AbstractSoapMessage implements SoapMessage{

    private Document document;
    @Override
    public SoapEnvelope getEnvelope() throws SoapEnvelopeException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getSoapAction() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setSoapAction(String soapAction) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Document getDocument() {
        return document;
    }

    @Override
    public void setDocument(Document document) {
        this.document = document;
    }

    @Override
    public boolean isXopPackage() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean convertToXopPackage() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Attachment getAttachment(String contentId) throws AttachmentException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Iterator<Attachment> getAttachments() throws AttachmentException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Attachment addAttachment(String contentId, DataHandler dataHandler) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
