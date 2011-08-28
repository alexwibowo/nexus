package org.isolution.nexus.test.support;

import org.isolution.nexus.domain.Service;
import org.isolution.nexus.domain.ServiceURI;
import org.isolution.nexus.domain.Status;

/**
 * User: Alex Wibowo
 * Date: 24/08/11
 * Time: 11:20 PM
 */
public class ServiceBuilder extends AbstractNexusModelBuilder<Service>{

    public ServiceBuilder() {
        withStatus(Status.ACTIVE);

    }
    @Override
    protected Service createNew() {
        return new Service();
    }

    public ServiceBuilder withStatus(Status value) {
        put("status", value);
        return this;
    }

    public ServiceBuilder withServiceURI(ServiceURI value) {
        put("serviceURI", value);
        return this;
    }


}
