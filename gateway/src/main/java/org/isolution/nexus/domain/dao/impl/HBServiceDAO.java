package org.isolution.nexus.domain.dao.impl;

import org.hibernate.Query;
import org.isolution.nexus.domain.Service;
import org.isolution.nexus.domain.ServiceURI;
import org.isolution.nexus.domain.dao.AbstractHBDAO;
import org.isolution.nexus.domain.dao.ServiceDAO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: agwibowo
 * Date: 2/01/11
 * Time: 9:27 PM
 */
@Repository
@Qualifier("hibernate")
public class HBServiceDAO extends AbstractHBDAO<Service> implements ServiceDAO {

    @Override
    public Service getEndpointByServiceURI(final String serviceURIString) {
        ServiceURI serviceURI = ServiceURI.parse(serviceURIString);

        Query query = getCurrentSession().createQuery("FROM Service s WHERE s.serviceURI.localName = :localName AND s.serviceURI.namespace= :namespace");
        query.setString("localName", serviceURI.getLocalName());
        query.setString("namespace", serviceURI.getNamespace());

        //noinspection unchecked
        return DataAccessUtils.requiredUniqueResult((List<Service>) query.list());
    }
}
