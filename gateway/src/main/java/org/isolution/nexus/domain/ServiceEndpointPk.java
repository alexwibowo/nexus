package org.isolution.nexus.domain;

import com.google.common.base.Objects;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * User: Alex Wibowo
 * Date: 26/01/11
 * Time: 9:23 AM
 */
@Embeddable
public class ServiceEndpointPk implements Serializable{

    @ManyToOne
    private Service service;

    @ManyToOne
    private Endpoint endpoint;

    public ServiceEndpointPk() {
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(service, endpoint);
    }

    @Override
    public boolean equals(Object o) {
        return Objects.equal(this, o);
    }
}
