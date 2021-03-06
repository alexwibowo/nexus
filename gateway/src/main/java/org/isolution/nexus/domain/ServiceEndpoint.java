package org.isolution.nexus.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
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


    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "created_date_time")
    protected DateTime createDateTime;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "updated_date_time")
    protected DateTime updateDateTime;


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

    public DateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(DateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public DateTime getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(DateTime updateDateTime) {
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
