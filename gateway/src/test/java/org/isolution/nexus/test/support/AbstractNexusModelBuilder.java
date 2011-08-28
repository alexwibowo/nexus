package org.isolution.nexus.test.support;

import org.isolution.nexus.domain.AbstractModel;
import org.isolution.test.support.AbstractBuilder;

import java.sql.Timestamp;

/**
 * User: Alex Wibowo
 * Date: 24/08/11
 * Time: 11:21 PM
 */
public abstract class AbstractNexusModelBuilder<M extends AbstractModel> extends AbstractBuilder<M>{

    protected AbstractNexusModelBuilder() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        withCreateDateTime(now)
                .withUpdateDateTime(now);
    }

    public AbstractNexusModelBuilder withCreateDateTime(Timestamp value) {
        put("createDateTime", value);
        return this;
    }

    public AbstractNexusModelBuilder withUpdateDateTime(Timestamp value) {
        put("updateDateTime", value);
        return this;
    }

}
