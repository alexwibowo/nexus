package org.isolution.nexus.domain.dao.impl;

import org.isolution.nexus.domain.Endpoint;
import org.isolution.nexus.domain.dao.AbstractHBDAO;
import org.isolution.nexus.domain.dao.EndpointDAO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * User: Alex Wibowo
 * Date: 30/12/10
 * Time: 8:58 PM
 */
@Repository
@Qualifier("hibernate")
public class HBEndpointDAO extends AbstractHBDAO<Endpoint> implements EndpointDAO {
}
