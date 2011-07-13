package org.isolution.nexus.invoker;

import com.google.common.base.Predicate;
import org.isolution.nexus.domain.EndpointProtocol;

/**
* User: agwibowo
* Date: 11/07/11
* Time: 9:55 PM
*/
class InvokerPredicate implements Predicate<Invoker> {
    static Predicate<Invoker> thatSupports(EndpointProtocol endpointProtocol) {
        return new InvokerPredicate(endpointProtocol);
    }

    private EndpointProtocol protocol;

    InvokerPredicate(EndpointProtocol protocol) {
        this.protocol = protocol;
    }

    @Override
    public boolean apply(Invoker input) {
        return input.supports(protocol);
    }
}
