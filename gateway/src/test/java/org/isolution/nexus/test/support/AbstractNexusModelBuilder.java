package org.isolution.nexus.test.support;

import org.isolution.nexus.domain.AbstractModel;
import org.isolution.test.support.AbstractBuilder;
import org.joda.time.DateTime;

/**
 * User: Alex Wibowo
 * Date: 24/08/11
 * Time: 11:21 PM
 */
public abstract class AbstractNexusModelBuilder<M extends AbstractModel> extends AbstractBuilder<M>{

    protected AbstractNexusModelBuilder() {
        DateTime now = new DateTime();
        withCreateDateTime(now)
                .withUpdateDateTime(now);
    }

    public AbstractNexusModelBuilder withCreateDateTime(DateTime value) {
        put("createDateTime", value);
        return this;
    }

    public AbstractNexusModelBuilder withUpdateDateTime(DateTime value) {
        put("updateDateTime", value);
        return this;
    }

}
