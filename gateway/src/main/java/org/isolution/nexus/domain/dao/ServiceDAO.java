package org.isolution.nexus.domain.dao;

import org.isolution.nexus.domain.Service;

/**
 * User: Alex Wibowo
 * Date: 26/01/11
 * Time: 10:18 AM
 */
public interface ServiceDAO extends DAO<Service>{

    Service getEndpointByServiceURI(final String serviceURI);
}
