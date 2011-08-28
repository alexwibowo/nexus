package org.isolution.nexus.gateway;


import org.isolution.nexus.invoker.InvokerController;
import org.isolution.nexus.test.support.MockMessageContext;
import org.isolution.nexus.xml.SOAPDocument;
import org.isolution.springframework.ws.MessageContextHolder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.Matchers;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapMessage;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MessageContextHolder.class)
public class NexusGatewayEndpointUnitTest {

    @Mock
    private XMLStreamReader mockStreamReader;

    @Mock
    private XMLStreamWriter mockStreamWriter;

    private MessageContext mockMessageContext;

    @Mock
    private SoapMessage mockSoapMessageRequest;

    @Mock
    private SOAPDocument mockSOAPDocumentRequest;


    @Mock
    private SoapMessage mockSoapMessageResponse;

    @Mock
    private SOAPDocument mockSOAPDocumentResponse;

    @Mock
    private NexusGatewayEndpointHelper helper;

    @Mock
    private InvokerController mockController;

    @Captor
    private ArgumentCaptor<SOAPDocument> controllerSOAPRequestCaptor;

    private NexusGatewayEndpoint gateway;

    @Before
    public void setup() throws Exception {
        gateway = new NexusGatewayEndpoint(mockController, helper);

        setupMockMessageContext();
        setupSOAPMessageReading();

        when(mockController.invoke(Matchers.eq(mockSOAPDocumentRequest))).thenReturn(mockSOAPDocumentResponse);
        when(mockSOAPDocumentResponse.getRawSoapMessage()).thenReturn(mockSoapMessageResponse);
    }

    private void setupMockMessageContext() {
        mockStatic(MessageContextHolder.class);
        mockMessageContext = new MockMessageContext();
        given(MessageContextHolder.getMessageContext()).willReturn(mockMessageContext);

        ((MockMessageContext) mockMessageContext).setRequest(mockSoapMessageRequest);
    }

    private void setupSOAPMessageReading() {
        given(helper.getSOAPDocument(eq(mockMessageContext), eq(mockStreamReader))).willReturn(mockSOAPDocumentRequest);
    }

    @Test
    public void should_attempt_to_get_soapRequest_from_the_reader() throws Exception {
        gateway.invokeInternal(mockStreamReader, mockStreamWriter);

        verify(helper).getSOAPDocument(Mockito.eq(mockMessageContext), Mockito.eq(mockStreamReader));
    }

    @Test
    public void should_send_the_SOAPDocument_request_to_the_controller() throws Exception {
        gateway.invokeInternal(mockStreamReader, mockStreamWriter);

        verify(mockController, times(1)).invoke(controllerSOAPRequestCaptor.capture());
        assertThat(controllerSOAPRequestCaptor.getValue(), org.hamcrest.Matchers.is(mockSOAPDocumentRequest));
    }

    @Test
    public void should_set_the_SOAPDocument_response_to_the_messageContext() throws Exception {
        gateway.invokeInternal(mockStreamReader, mockStreamWriter);

        assertThat((SoapMessage)mockMessageContext.getResponse(), org.hamcrest.Matchers.is(mockSoapMessageResponse));
    }
}
