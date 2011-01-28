package org.isolution.nexus.routing.impl;

import org.apache.commons.lang.NotImplementedException;
import org.isolution.nexus.domain.Endpoint;
import org.isolution.nexus.domain.Service;
import org.isolution.nexus.domain.ServiceEndpoint;
import org.isolution.nexus.domain.ServiceURI;
import org.isolution.nexus.domain.dao.EndpointDAO;
import org.isolution.nexus.domain.dao.ServiceDAO;
import org.isolution.nexus.routing.NoActiveRouteException;
import org.isolution.nexus.routing.ServiceNotFoundException;
import org.isolution.nexus.routing.ServiceRouter;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

/**
 * User: agwibowo
 * Date: 30/12/10
 * Time: 8:45 PM
 */
public class DatabaseServiceRouter implements ServiceRouter{

    private EndpointDAO endpointDAO;

    private ServiceDAO serviceDAO;

    public DatabaseServiceRouter(ServiceDAO serviceDAO, EndpointDAO endpointDAO) {
        this.endpointDAO = endpointDAO;
        this.serviceDAO = serviceDAO;
    }

    @Override
    public Endpoint findSingleActiveEndpoint(final String serviceURIString)
            throws ServiceNotFoundException, NoActiveRouteException {
        Service service = getService(serviceURIString);
        for (ServiceEndpoint serviceEndpoint : service.getServiceEndpoints()) {
            if (serviceEndpoint.isRoutable()) {
                return serviceEndpoint.getEndpoint();
            }
        }
        throw NoActiveRouteException.noActiveEndpointForService(service);
    }

    private Service getService(String serviceURIString) throws ServiceNotFoundException {
        Service service = null;
        try {
            service = serviceDAO.getEndpointByServiceURI(serviceURIString);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new ServiceNotFoundException(serviceURIString);
        }
        return service;
    }
}
