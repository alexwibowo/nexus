package org.isolution.nexus.gateway;


import org.isolution.nexus.domain.Endpoint;
import org.isolution.nexus.domain.ServiceURI;
import org.isolution.nexus.invoker.Invoker;
import org.isolution.nexus.invoker.InvokerResolver;
import org.isolution.nexus.routing.ServiceRouter;
import org.isolution.nexus.xml.SOAPDocument;
import org.isolution.springframework.ws.MessageContextHolder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.context.MessageContext;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import java.io.IOException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MessageContextHolder.class)
public class NexusGatewayEndpointUnitTest {

    @Mock
    private ServiceRouter mockServiceRouter;

    @Mock
    private InvokerResolver mockInvokerResolver;

    @Mock
    private XMLStreamReader mockStreamReader;

    @Mock
    private XMLStreamWriter mockStreamWriter;

    @Mock
    private MessageContext mockMessageContext;

    @Mock
    private WebServiceMessage mockWebServiceMessage;

    @Mock
    private Endpoint targetEndpoint;

    @Mock
    private Invoker mockInvoker;

    @Mock
    private NexusGatewayEndpointHelper helper;

    private NexusGatewayEndpoint gateway;

    @Mock
    private SOAPDocument mockSOAPRequest;

    @Mock
    private SOAPDocument mockSOAPResponse;

    @Mock
    private ServiceURI mockServiceURI;

    @Before
    public void setup() throws Exception {
        gateway = new NexusGatewayEndpoint(mockServiceRouter, mockInvokerResolver, helper);

        setupMockMessageContext();
        setupSOAPMessageReading();
        given(mockServiceRouter.findSingleActiveEndpoint("http://www.google.com/GetPriceReq")).willReturn(targetEndpoint);
        setupMockInvocation();
    }

    private void setupMockInvocation() throws IOException {
        given(mockInvokerResolver.resolveForEndpoint(targetEndpoint)).willReturn(mockInvoker);
        given(mockInvoker.invoke(Mockito.<SOAPDocument>any())).willReturn(mockSOAPResponse);
    }

    private void setupMockMessageContext() {
        mockStatic(MessageContextHolder.class);
        given(MessageContextHolder.getMessageContext()).willReturn(mockMessageContext);
        given(mockMessageContext.getRequest()).willReturn(mockWebServiceMessage);
    }

    private void setupSOAPMessageReading() {
        given(helper.getSOAPDocument(eq(mockMessageContext), eq(mockStreamReader))).willReturn(mockSOAPRequest);
        given(mockSOAPRequest.getServiceURI()).willReturn(mockServiceURI);
        given(mockServiceURI.getServiceURIString()).willReturn("http://www.google.com/GetPriceReq");
    }

    @Test
    public void should_use_soapRequest_to_deduce_serviceURI() throws Exception {
        gateway.invokeInternal(mockStreamReader, mockStreamWriter);
        verify(helper).getSOAPDocument(Mockito.eq(mockMessageContext), Mockito.eq(mockStreamReader));
    }

    @Test
    public void should_resolve_endpoint_for_the_request() throws Exception {
        gateway.invokeInternal(mockStreamReader, mockStreamWriter);
        verify(mockServiceRouter).findSingleActiveEndpoint("http://www.google.com/GetPriceReq");
    }

    @Test
    public void should_send_the_request_to_the_resolved_endpoint() throws Exception {
        gateway.invokeInternal(mockStreamReader, mockStreamWriter);

        InOrder inOrder = inOrder(mockInvokerResolver, mockInvoker);
        inOrder.verify(mockInvokerResolver).resolveForEndpoint(targetEndpoint);
        inOrder.verify(mockInvoker).invoke(mockSOAPRequest);
    }

    @Test
    public void should_write_response_to_the_stream_writer() throws Exception {
        gateway.invokeInternal(mockStreamReader, mockStreamWriter);
        verify(mockSOAPResponse).writeTo(Mockito.eq(mockStreamWriter));
    }
}
