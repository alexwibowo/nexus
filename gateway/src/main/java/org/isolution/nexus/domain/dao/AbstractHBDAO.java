package org.isolution.nexus.domain.dao;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.isolution.nexus.domain.AbstractModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Generic DAO implementation using Hibernate
 *
 * User: agwibowo
 * Date: 30/12/10
 * Time: 8:57 PM
 */
public abstract class AbstractHBDAO<M  extends AbstractModel> implements DAO<M>{

    protected Class<M> clazz;

    @Autowired
    protected SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    protected AbstractHBDAO() {
        this.clazz= (Class<M>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public M save(M value) {
        Timestamp currentTimestamp = new Timestamp(new Date().getTime());
        if (value.getId() == null) {
            value.setCreateDateTime(currentTimestamp);
        }
        value.setUpdateDateTime(currentTimestamp);
        getCurrentSession().merge(value);
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public M get(Long id) {
        return (M) getCurrentSession().get(clazz, id);
    }

    @Override
    public void delete(M value) {
        getCurrentSession().delete(value);
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
