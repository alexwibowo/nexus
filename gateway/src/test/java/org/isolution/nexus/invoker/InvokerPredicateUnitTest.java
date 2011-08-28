package org.isolution.nexus.invoker;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import org.isolution.nexus.domain.EndpointProtocol;
import org.isolution.nexus.invoker.impl.AbstractInvoker;
import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Iterators.find;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * User: Alex Wibowo
 * Date: 11/07/11
 * Time: 10:16 PM
 */
public class InvokerPredicateUnitTest {

    private static class TestHttpInvoker extends AbstractInvoker{
        @Override
        public boolean supports(EndpointProtocol protocol) {
            return EndpointProtocol.HTTP.equals(protocol);
        }
    }

    private static class TestFtpInvoker extends AbstractInvoker{
        @Override
        public boolean supports(EndpointProtocol protocol) {
            return EndpointProtocol.FTP.equals(protocol);
        }
    }

    @Test
    public void should_be_able_to_find_invoker_that_supports_a_given_protocol() {
        Invoker httpInvoker = new TestHttpInvoker();
        Invoker ftpInvoker = new TestFtpInvoker();
        List<Invoker> invokers = Lists.<Invoker>newArrayList(httpInvoker, ftpInvoker);

        Predicate<Invoker> predicate = InvokerPredicate.thatSupports(EndpointProtocol.HTTP);
        Invoker invoker = find(invokers.iterator(), predicate);
        assertThat(invoker, not(nullValue()));
        assertThat(invoker, is(httpInvoker));
    }

}
