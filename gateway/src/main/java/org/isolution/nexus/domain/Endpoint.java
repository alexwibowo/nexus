package org.isolution.nexus.domain;

import org.isolution.common.validation.ValidURIString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * User: agwibowo
 * Date: 30/12/10
 * Time: 8:37 PM
 */
@Entity
@Table(name = "endpoint")
@SequenceGenerator(name = "id_generator", sequenceName = "endpoint_seq")
public class Endpoint extends AbstractModel {

    /**
     * URI for the endpoint. i.e. how the endpoint is to be contactable
     */
    @ValidURIString
    @NotNull
    @Column(name = "uri")
    private String uri;

    /**
     * Protocol for the endpoint
     */
    @Column(name = "protocol")
    @Enumerated(value = EnumType.STRING)
    private EndpointProtocol protocol;

    /**
     * List of {@link ServiceEndpoint} associated with this endpoint
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id.endpoint", orphanRemoval = true)
    private List<ServiceEndpoint> serviceEndpoints;


    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.ACTIVE;

    public Endpoint() {
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public EndpointProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(EndpointProtocol protocol) {
        this.protocol = protocol;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void activate() {
        setStatus(Status.ACTIVE);
    }

    public void inactivate() {
        setStatus(Status.INACTIVE);
    }


    public List<ServiceEndpoint> getServiceEndpoints() {
        return serviceEndpoints;
    }

    public void setServiceEndpoints(List<ServiceEndpoint> serviceEndpoints) {
        this.serviceEndpoints = serviceEndpoints;
    }

    @Transient
    public List<Service> getServices() {
        List<Service> services = new ArrayList<Service>();
        for (ServiceEndpoint serviceEndpoint : serviceEndpoints) {
            services.add(serviceEndpoint.getService());
        }
        return services;
    }

    @Override
    public String toString() {
        return uri;
    }

    public URI toURI() {
        try {
            return new URI(uri);
        } catch (Exception e) {
            throw new IllegalStateException("Invalid URI has been used", e);
        }
    }

    public boolean isActive() {
        return Status.ACTIVE.equals(getStatus());
    }
}
