package org.isolution.nexus.domain;

import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * User: agwibowo
 * Date: 26/01/11
 * Time: 9:23 AM
 */
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
}
