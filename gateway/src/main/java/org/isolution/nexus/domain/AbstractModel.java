package org.isolution.nexus.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User: Alex Wibowo
 * Date: 21/12/10
 * Time: 12:09 AM
 */
@MappedSuperclass
public abstract class AbstractModel implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "id_generator", strategy = GenerationType.SEQUENCE)
    protected Long id;

    @Version
    @Column(name = "version")
    protected Long version;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "created_date_time")
    protected DateTime createDateTime;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "updated_date_time")
    protected DateTime updateDateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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
}
