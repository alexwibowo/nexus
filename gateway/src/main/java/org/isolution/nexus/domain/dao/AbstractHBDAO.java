package org.isolution.nexus.domain.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.isolution.nexus.domain.AbstractModel;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Generic DAO implementation using Hibernate
 *
 * User: Alex Wibowo
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
    @SuppressWarnings("unchecked")
    public M save(M value) {
        DateTime currentTimestamp = new DateTime();
        if (value.getId() == null) {
            value.setCreateDateTime(currentTimestamp);
        }
        value.setUpdateDateTime(currentTimestamp);
        return (M) getCurrentSession().merge(value);
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

    @Override
    public void deleteAll() {
        for (M instance : loadAll()) {
            getCurrentSession().delete(instance);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<M> loadAll() {
        Criteria criteria = getCurrentSession().createCriteria(clazz);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
