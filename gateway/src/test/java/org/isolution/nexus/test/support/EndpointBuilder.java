package org.isolution.nexus.test.support;

import org.isolution.nexus.domain.Endpoint;
import org.isolution.nexus.domain.EndpointProtocol;
import org.isolution.nexus.domain.Status;

/**
 * User: Alex Wibowo
 * Date: 24/08/11
 * Time: 11:11 PM
 */
public class EndpointBuilder extends AbstractNexusModelBuilder<Endpoint>{

    public EndpointBuilder() {
        withEndpointProtocol(EndpointProtocol.HTTP)
                .withStatus(Status.ACTIVE);
    }

    @Override
    protected Endpoint createNew() {
        return new Endpoint();
    }

    public EndpointBuilder withUri(String value) {
        put("uri", value);
        return this;
    }

    public EndpointBuilder withEndpointProtocol(EndpointProtocol value) {
        put("protocol", value);
        return this;
    }

    public EndpointBuilder withStatus(Status value) {
        put("status", value);
        return this;
    }


}
