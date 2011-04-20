package org.isolution.nexus.invoker.impl;

import org.isolution.nexus.invoker.InvocationException;
import org.isolution.nexus.xml.SOAPDocument;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.http.CommonsHttpMessageSender;
import org.springframework.ws.transport.http.HttpUrlConnection;

import java.io.IOException;
import java.net.URI;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * User: agwibowo
 * Date: 19/04/11
 * Time: 10:25 PM
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(HTTPInvoker.class)
public class HTTPInvokerUnitTest {
    @Mock
    private CommonsHttpMessageSender mockSender;

    @Mock
    private WebServiceConnection mockConnection;

    @Mock
    private SOAPDocument mockSOAPDocument;

    @Mock
    private SoapMessage mockSOAPMessage;

    @Before
    public void setup() throws Exception {
        whenNew(CommonsHttpMessageSender.class).withNoArguments().thenReturn(mockSender);

        when(mockSOAPDocument.getRawSoapMessage()).thenReturn(mockSOAPMessage);

        when(mockSender.createConnection(Mockito.<URI>any())).thenReturn(mockConnection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_given_null_URL() {
        new HTTPInvoker(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_given_blank_URL() {
        new HTTPInvoker("");
    }

    @Test
    public void should_establish_connection_with_the_given_URL() throws Exception {
        String url = "http://www.google.com";
        HTTPInvoker httpInvoker = new HTTPInvoker(url);
        httpInvoker.invoke(mockSOAPDocument);

        verifyNew(CommonsHttpMessageSender.class).withNoArguments();

        ArgumentCaptor<URI> uriArgumentCaptor = ArgumentCaptor.forClass(URI.class);
        verify(mockSender, times(1)).createConnection(uriArgumentCaptor.capture());
        assertThat(uriArgumentCaptor.getValue().toURL().toString(), is(url));
    }

    @Test
    public void should_throw_exception_when_cant_establish_connection() throws Exception {
        IOException ioex = new IOException();
        when(mockSender.createConnection(Mockito.<URI>any())).thenThrow(ioex);

        try {
            new HTTPInvoker("http://www.google.com").invoke(mockSOAPDocument);
            fail("Should have failed");
        } catch (IOException e) {
            assertThat(e, is(ioex));
        }
    }

    @Test
    public void should_write_the_soapDocument_to_the_connection() throws Exception {
        HTTPInvoker httpInvoker = new HTTPInvoker("http://www.google.com");
        httpInvoker.invoke(mockSOAPDocument);

        ArgumentCaptor<SoapMessage> captor = ArgumentCaptor.forClass(SoapMessage.class);
        verify(mockConnection, times(1)).send(captor.capture());
        assertThat(captor.getValue(), is(mockSOAPMessage));
    }

    @Test
    public void should_throw_exception_when_cant_send_document() throws Exception {
        IOException ioex = new IOException();
        doThrow(ioex).when(mockConnection).send(Mockito.<WebServiceMessage>any());

        try {
            new HTTPInvoker("http://www.google.com").invoke(mockSOAPDocument);
            fail("Should have failed");
        } catch (IOException e) {
            assertThat(e, is(ioex));
        }
    }

    @Test
    public void should_close_connection_when_everything_went_ok() throws Exception{
        HTTPInvoker httpInvoker = new HTTPInvoker("http://www.google.com");
        httpInvoker.invoke(mockSOAPDocument);

        verify(mockConnection, times(1)).close();
    }

    @Test
    public void should_still_close_connection_on_failure_to_send_document() throws Exception {
        doThrow(new IOException()).when(mockConnection).send(Mockito.<WebServiceMessage>any());

        try {
            new HTTPInvoker("http://www.google.com").invoke(mockSOAPDocument);
            fail("Should have failed");
        } catch (Throwable ignore) {
        }

        verify(mockConnection, times(1)).close();
    }

    @Test
    public void should_throw_invocation_exception_when_response_code_is_error() throws Exception {
        when(mockConnection.hasError()).thenReturn(true);
        String errorMessage = "Yeah. Thats an error mate";
        when(mockConnection.getErrorMessage()).thenReturn(errorMessage);

        try {
            new HTTPInvoker("http://www.google.com").invoke(mockSOAPDocument);
            fail("Should have failed");
        } catch (InvocationException ex) {
            assertThat(ex.getMessage(), is(errorMessage));
        }
    }

    @Test
    public void should_still_close_the_connection_when_response_code_is_error() throws Exception {
        when(mockConnection.hasError()).thenReturn(true);
        when(mockConnection.getErrorMessage()).thenReturn("Yeah. Thats an error mate");

        try {
            new HTTPInvoker("http://www.google.com").invoke(mockSOAPDocument);
        } catch (Throwable ignore) {
        }

        verify(mockConnection, times(1)).close();
    }
}
