package org.isolution.nexus.domain;

import com.google.common.base.Function;
import org.isolution.common.validation.ValidURIString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

import static com.google.common.collect.Lists.transform;

/**
 * User: Alex Wibowo
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
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id.endpoint", orphanRemoval = true,  cascade = CascadeType.ALL)
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

    public void deactivate() {
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
        return transform(serviceEndpoints, new Function<ServiceEndpoint, Service>() {
            @Override
            public Service apply(ServiceEndpoint serviceEndpoint) {
                return serviceEndpoint.getService();
            }
        });
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
