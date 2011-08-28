package org.isolution.nexus.domain.dao;

import javassist.expr.Instanceof;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.isolution.nexus.domain.AbstractModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.sql.Timestamp;
import java.util.Date;
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
    public M save(M value) {
        Timestamp currentTimestamp = new Timestamp(new Date().getTime());
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
