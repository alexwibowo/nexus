package org.isolution.nexus.invoker.impl;

import org.isolution.nexus.xml.SOAPDocument;
import org.isolution.nexus.xml.SOAPDocumentFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.SoapMessageCreationException;
import org.springframework.ws.soap.SoapMessageFactory;
import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.http.CommonsHttpMessageSender;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.doThrow;
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

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private SOAPDocument mockSOAPDocument;

    @Mock
    private SoapMessage mockSOAPMessage;

    @Mock
    private Source mockSource;

    @Mock
    private SoapMessageFactory mockSOAPMessageFactory;

    @Mock
    private SOAPDocumentFactory mockSOAPDocumentFactory;

    @Mock
    private WebServiceTemplate mockWebServiceTemplate;

    private HTTPInvoker httpInvoker;

    @Before
    public void setup() throws Exception {
        initMocks(this);
        whenNew(CommonsHttpMessageSender.class).withNoArguments().thenReturn(mockSender);

        when(mockSOAPDocument.getRawSoapMessage().getPayloadSource()).thenReturn(mockSource);
        when(mockSender.createConnection(Mockito.<URI>any())).thenReturn(mockConnection);

        httpInvoker = new HTTPInvoker(mockWebServiceTemplate, mockSOAPDocumentFactory, null);
        httpInvoker.setTarget(getURI("http://www.kubi.com"));
    }

    @Test(expected = NullPointerException.class)
    public void should_throw_exception_when_given_null_URL() {
        httpInvoker.setTarget(null);
    }

    private URI getURI(String urlString) throws Exception {
        URL url = new URL(urlString);
        return url.toURI();
    }

    @Test
    public void should_establish_connection_with_the_given_URL() throws Exception {
        String url = "http://www.google.com";
        httpInvoker.setTarget(getURI(url));
        httpInvoker.invoke(mockSOAPDocument);

        ArgumentCaptor<String> uriArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockWebServiceTemplate, times(1)).sendSourceAndReceiveToResult(
                uriArgumentCaptor.capture(),
                Mockito.<Source>any(Source.class),
                Mockito.<Result>any(Result.class)
        );
        assertThat(uriArgumentCaptor.getValue(), is(url));
    }

    @Test
    public void should_send_the_request_document() throws Exception {
        httpInvoker.invoke(mockSOAPDocument);
        verify(mockWebServiceTemplate, times(1)).sendSourceAndReceiveToResult(
                anyString(),
                eq(mockSource),
                Mockito.<Result>any(Result.class)
        );
    }

    @Test
    public void should_construct_soapDocument_from_the_response() throws Exception {
        StringWriter mock = mock(StringWriter.class);
        whenNew(StringWriter.class).withNoArguments().thenReturn(mock);
        when(mock.toString()).thenReturn("<soap/>");

        httpInvoker.invoke(mockSOAPDocument);

        ArgumentCaptor<String> responseCapture = ArgumentCaptor.forClass(String.class);
        verify(mockSOAPDocumentFactory).createSOAPResponse(responseCapture.capture());
        assertThat(responseCapture.getValue(), is("<soap/>"));
    }


    @Test
    public void should_return_soapDocument_returned_from_invocation() throws Exception {
        StringWriter mock = mock(StringWriter.class);
        whenNew(StringWriter.class).withNoArguments().thenReturn(mock);

        SOAPDocument mockResponse = mock(SOAPDocument.class);
        when(mockSOAPDocumentFactory.createSOAPResponse(Mockito.anyString())).thenReturn(mockResponse);

        SOAPDocument actualResponse = httpInvoker.invoke(mockSOAPDocument);
        assertThat(actualResponse, is(mockResponse));
    }

    @Test
    public void should_throw_exception_on_error_to_construct_response() throws Exception {
        StringWriter mock = mock(StringWriter.class);
        whenNew(StringWriter.class).withNoArguments().thenReturn(mock);
        when(mock.toString()).thenReturn("<soap/>");

        XMLStreamException xmlex = new XMLStreamException();
        doThrow(xmlex).when(mockSOAPDocumentFactory).createSOAPResponse(anyString());

        try {
            httpInvoker.invoke(mockSOAPDocument);
            fail("Should have failed due to failure to create soap response document");
        } catch (SoapMessageCreationException e) {
            assertThat(e.getCause(), org.hamcrest.Matchers.instanceOf(XMLStreamException.class));
            assertThat((XMLStreamException) e.getCause(), is(xmlex));
        }
    }

    @Test
    public void should_throw_exception_when_cant_send_message() throws Exception {
        WebServiceIOException ioex = new WebServiceIOException("Hey hey");
        doThrow(ioex)
                .when(mockWebServiceTemplate)
                .sendSourceAndReceiveToResult(anyString(), Mockito.<Source>any(), Mockito.<Result>any());

        try {
            httpInvoker.setTarget(getURI("http://www.google.com"));
            httpInvoker.invoke(mockSOAPDocument);
            fail("Should have failed");
        } catch (WebServiceIOException e) {
            assertThat(e, is(ioex));
        }
    }
}
