package org.isolution.nexus.invoker;

import org.isolution.nexus.domain.EndpointProtocol;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

/**
 * User: agwibowo
 * Date: 18/05/11
 * Time: 8:40 PM
 */
@RunWith(MockitoJUnitRunner.class)
public class InvokerResolverUnitTest {

    private InvokerResolver resolver;

    @Mock
    private Invoker mockHTTPInvoker;

    @Mock
    private Invoker mockFTPInvoker;

    @Before
    public void setup() {
        resolver = new InvokerResolver();
        when(mockHTTPInvoker.supports(EndpointProtocol.HTTP)).thenReturn(true);
        when(mockHTTPInvoker.supports(EndpointProtocol.FTP)).thenReturn(false);
        when(mockFTPInvoker.supports(EndpointProtocol.HTTP)).thenReturn(true);
        when(mockFTPInvoker.supports(EndpointProtocol.FTP)).thenReturn(true);
        resolver.register(mockHTTPInvoker);
    }

    @Test
    public void resolveForProtocol_should_return_invoker_that_supports_the_protocol_if_registered() throws Exception {
        assertThat(resolver.resolveForProtocol(EndpointProtocol.HTTP), is(mockHTTPInvoker));
    }

    @Test
    public void resolveForProtocol_should_throw_exception_if_none_registered() throws Exception {
        try {
            resolver.resolveForProtocol(EndpointProtocol.FTP);
            fail("Should have failed");
        } catch (Exception e) {
            assertThat(e, instanceOf(UnsupportedOperationException.class));
            assertThat(e.getMessage(),is("Protocol [FTP] is not supported.")  );
        }
    }
}
