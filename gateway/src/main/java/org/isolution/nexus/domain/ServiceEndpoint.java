package org.isolution.nexus.domain;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.MessageFormat;

/**
 * User: Alex Wibowo
 * Date: 26/01/11
 * Time: 9:15 AM
 */
@Entity
@Table(name = "service_endpoint")
@AssociationOverrides({
        @AssociationOverride(
                name = "id.service",
                joinColumns = @JoinColumn(name = "service_id")
        ),
        @AssociationOverride(
                name = "id.endpoint",
                joinColumns = @JoinColumn(name = "endpoint_id")
        )
})
public class ServiceEndpoint {

    @EmbeddedId
    private ServiceEndpointPk id = new ServiceEndpointPk();

   @Enumerated(value=EnumType.STRING)
    @Column(name="status")
   private Status status=Status.ACTIVE;


    @Column(name = "created_date_time")
    protected Timestamp createDateTime;

    @Column(name = "updated_date_time")
    protected Timestamp updateDateTime;


    public ServiceEndpointPk getId() {
        return id;
    }

    public void setId(ServiceEndpointPk id) {
        this.id = id;
    }

    @Transient
    public Service getService() {
        return getId().getService();
    }

    public void setService(Service service) {
        getId().setService(service);
    }

    @Transient
    public Endpoint getEndpoint() {
        return getId().getEndpoint();
    }

    public void setEndpoint(Endpoint endpoint) {
        getId().setEndpoint(endpoint);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Timestamp getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Timestamp createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Timestamp getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(Timestamp updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public void activate() {
          setStatus(Status.ACTIVE);
      }

      public void inactivate() {
          setStatus(Status.INACTIVE);
      }


    public boolean isActive() {
        return Status.ACTIVE.equals(getStatus());
    }

    public boolean isRoutable() {
        if (getEndpoint() == null) {
            throw new IllegalStateException(MessageFormat.format("Service Endpoint for service [{0}] doesnt have any endpoint associated to it. Application is in inconsistent state", getService().getId()));
        }
        return isActive() && getEndpoint().isActive();
    }
}
