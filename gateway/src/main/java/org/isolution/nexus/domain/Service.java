package org.isolution.nexus.domain;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * User: Alex Wibowo
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id.service", orphanRemoval = true, cascade = CascadeType.ALL)
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

    /**
     * Change the status of this sevice to {@link Status#ACTIVE}
     * Note that this method does NOT change the status of all the {@link #serviceEndpoints}.
     * This is because the status of the relationship is also driven from {@link Endpoint}
     */
    public void activate() {
        setStatus(Status.ACTIVE);
    }

    /**
     * Change the status of this service to {@link Status#INACTIVE}
     * This method also change the status of all the {@link #serviceEndpoints}
     * to {@link Status#INACTIVE}.
     */
    public void inactivate() {
        setStatus(Status.INACTIVE);

        // inactivating a service theoretically sets all the relationship as inactive too!
        for (ServiceEndpoint serviceEndpoint : serviceEndpoints) {
            serviceEndpoint.inactivate();
        }
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
        Timestamp now = new Timestamp(System.currentTimeMillis());
        se.setCreateDateTime(now);
        se.setUpdateDateTime(now);
        se.setStatus(status);
        ServiceEndpointPk id1 = new ServiceEndpointPk();
        id1.setEndpoint(endpoint);
        id1.setService(this);
        se.setId(id1);
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
        // for security purpose. adding endpoint should be done through addEndpoint() method
        return Collections.unmodifiableList(endpoints);
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

        // for security purpose. adding endpoint should be done through addEndpoint() method
        return Collections.unmodifiableList(endpoints);
    }

    public String toString() {
        return getServiceURI().toString();
    }

    public boolean isActive() {
        return Status.ACTIVE.equals(getStatus());
    }
}
