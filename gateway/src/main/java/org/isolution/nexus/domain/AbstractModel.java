package org.isolution.nexus.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * User: agwibowo
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

    @Column(name = "created_date_time")
    protected Timestamp createDateTime;

    @Column(name = "updated_date_time")
    protected Timestamp updateDateTime;

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
}
