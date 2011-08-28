package org.isolution.test.support;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Alex Wibowo
 * Date: 24/08/11
 * Time: 11:09 PM
 */
public abstract class AbstractBuilder<M> {

    protected Map<String, Object> propertyValues = new HashMap<String, Object>();


    protected void put(String property, Object value) {
        propertyValues.put(property, value);
    }

    protected abstract M createNew();

    public M build() throws InvocationTargetException, IllegalAccessException {
        M result = createNew();
        BeanUtils.populate(result, propertyValues);
        return result;
    }
}
