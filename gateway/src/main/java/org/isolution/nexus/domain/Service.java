package org.isolution.nexus.domain;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * User: agwibowo
 * Date: 21/12/10
 * Time: 12:08 AM
 */
@Entity
@Table(name = "service")
@SequenceGenerator(name = "id_generator", sequenceName = "service_seq")
public class Service extends AbstractModel {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "namespace", column = @Column(name = "namespace")),
            @AttributeOverride(name = "localName", column = @Column(name = "local_name"))
    })
    private ServiceURI serviceURI;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id.service", orphanRemoval = true)
    private List<ServiceEndpoint> serviceEndpoints = new ArrayList<ServiceEndpoint>();

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.ACTIVE;

    public Service() {
    }

    public Service(String namespace, String localName) {
        this.serviceURI = new ServiceURI(namespace, localName);
    }

    public ServiceURI getServiceURI() {
        return serviceURI;
    }

    public void setServiceURI(ServiceURI serviceURI) {
        this.serviceURI = serviceURI;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<ServiceEndpoint> getServiceEndpoints() {
        return serviceEndpoints;
    }

    public void setServiceEndpoints(List<ServiceEndpoint> serviceEndpoints) {
        this.serviceEndpoints = serviceEndpoints;
    }

    public void addEndpoint(Endpoint endpoint, Status status) {
        ServiceEndpoint se = new ServiceEndpoint();
        se.setEndpoint(endpoint);
        se.setService(this);
        se.setStatus(status);
        getServiceEndpoints().add(se);
    }

    /**
     * @return all {@link Endpoint} associated with this service, or empty list if there is no endpoint associated with this service
     */
    @Transient
    public List<Endpoint> getEndpoints() {
        List<Endpoint> endpoints = new ArrayList<Endpoint>();
        for (ServiceEndpoint serviceEndpoint : serviceEndpoints) {
            endpoints.add(serviceEndpoint.getEndpoint());
        }
        return endpoints;
    }

    /**
     * @return active {@link Endpoint} associated with this service, empty list otherwise
     */
    @Transient
    public List<Endpoint> getActiveEndpoints() {
        List<Endpoint> endpoints = new ArrayList<Endpoint>();
        for (ServiceEndpoint serviceEndpoint : serviceEndpoints) {
            if (serviceEndpoint.isActive() && serviceEndpoint.getEndpoint().isActive()) {
                endpoints.add(serviceEndpoint.getEndpoint());
            }
        }
        return endpoints;
    }

    public String toString() {
        return getServiceURI().toString();
    }

    public boolean isActive() {
        return Status.ACTIVE.equals(getStatus());
    }
}
