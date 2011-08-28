package org.isolution.nexus.routing;

import org.isolution.nexus.domain.Endpoint;
import org.isolution.nexus.domain.Service;

/**
 * User: Alex Wibowo
 * Date: 30/12/10
 * Time: 8:32 PM
 */
public interface ServiceRouter {

    /**
     * Find a single active endpoint for the given serviceURI. ServiceURI
     * is a concept at service level, with a service can be associated with one
     * or many endpoints. The endpoint itself can be active/inactive, and the same
     * goes with the service. Futhermore, the relationship between service & endpoint
     * can be activated/inactivated.
     *
     * This method returns an endpoint if and only if :
     * <ul>
     *  <li>The service is active</li>
     *  <li>The endpoint is active</li>
     *  <li>The relationship between the service and the endpoint is active</li>
     * <ul>
     *
     * otherwise an appropriate exception will be thrown
     *
     * @param serviceURIString String representation of Service URI, in a format that can be understood by {@link org.isolution.nexus.domain.ServiceURI}
     * @return endpoint active endpoint for the requested serviceURI
     * @throws ServiceNotFoundException  if the service cannot be found
     * @throws NoActiveRouteException  if there's no active endpoint, or no active relationship between service & endpoint
     * @throws InactiveServiceException  if the service is found, but inactive
     */
    Endpoint findSingleActiveEndpoint(String serviceURIString)
            throws ServiceNotFoundException, NoActiveRouteException, InactiveServiceException;

    /**
     * Find service that has the requested {@link Service#serviceURI} string,
     * matching against {@link org.isolution.nexus.domain.ServiceURI#namespace}
     * and {@link org.isolution.nexus.domain.ServiceURI#localName}.
     *
     * @param serviceURIString String representation of Service URI, in a format that can be understood by {@link org.isolution.nexus.domain.ServiceURI}
     * @return  service that matches the requested serviceURI
     * @throws InactiveServiceException  if the service is found, but inactive
     * @throws ServiceNotFoundException if the service cannot be found
     */
    Service findActiveService(String serviceURIString) throws InactiveServiceException, ServiceNotFoundException;
}
