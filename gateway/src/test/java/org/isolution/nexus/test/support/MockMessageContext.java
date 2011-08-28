package org.isolution.nexus.test.support;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapMessage;

import java.io.IOException;
import java.io.InputStream;

/**
 * User: Alex Wibowo
 * Date: 24/08/11
 * Time: 11:57 PM
 */
public class MockMessageContext implements MessageContext {

    private WebServiceMessage request;

    private WebServiceMessage response;

    public WebServiceMessage getRequest() {
        return request;
    }

    public void setRequest(WebServiceMessage request) {
        this.request = request;
    }

    public WebServiceMessage getResponse() {
        return response;
    }

    public void setResponse(WebServiceMessage response) {
        this.response = response;
    }

    @Override
    public boolean hasResponse() {
        return response !=null;
    }


    @Override
    public void clearResponse() {
        response = null;
    }

    @Override
    public void readResponse(InputStream inputStream) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setProperty(String name, Object value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object getProperty(String name) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeProperty(String name) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean containsProperty(String name) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String[] getPropertyNames() {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }
}
