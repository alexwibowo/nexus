package org.isolution.nexus.invoker;

import org.isolution.nexus.domain.EndpointProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import static com.google.common.collect.Iterators.find;
import static org.isolution.nexus.invoker.InvokerPredicate.thatSupports;

/**
 * User: agwibowo
 * Date: 30/04/11
 * Time: 6:26 PM
 */
@Component
public class InvokerResolver {

    @Autowired
    private Set<Invoker<URI>> invokers;

    public InvokerResolver() {
        invokers=new HashSet<Invoker<URI>>();
    }

    public Invoker<URI> resolveForProtocol(EndpointProtocol protocol) {
        try {
            return find(invokers.iterator(), thatSupports(protocol));
        } catch (NoSuchElementException e) {
            throw new UnsupportedOperationException(String.format("Protocol [%s] is not supported.", protocol));
        }
    }


    public void register(Invoker<URI> invoker) {
        invokers.add(invoker);
    }
}
