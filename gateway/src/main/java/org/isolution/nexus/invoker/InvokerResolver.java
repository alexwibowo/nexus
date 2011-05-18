package org.isolution.nexus.invoker;

import com.google.common.base.Predicate;
import org.isolution.nexus.domain.EndpointProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import static com.google.common.collect.Iterators.find;

/**
 * User: agwibowo
 * Date: 30/04/11
 * Time: 6:26 PM
 */
@Component
public class InvokerResolver {

    @Autowired
    private Set<Invoker> invokers;

    public InvokerResolver() {
        invokers=new HashSet<Invoker>();
    }

    public Invoker resolveForProtocol(EndpointProtocol protocol) {
        try {
            return find(invokers.iterator(), InvokerPredicate.thatSupports(protocol));
        } catch (NoSuchElementException e) {
            throw new UnsupportedOperationException(String.format("Protocol [%s] is not supported.", protocol));
        }
    }


    private static class InvokerPredicate implements Predicate<Invoker> {
        public static Predicate<Invoker> thatSupports(EndpointProtocol endpointProtocol) {
            return new InvokerPredicate(endpointProtocol);
        }

        private EndpointProtocol protocol;

        private InvokerPredicate(EndpointProtocol protocol) {
            this.protocol = protocol;
        }

        @Override
        public boolean apply(Invoker input) {
            return input.supports(protocol);
        }
    }


    public void register(Invoker invoker) {
        invokers.add(invoker);
    }
}
