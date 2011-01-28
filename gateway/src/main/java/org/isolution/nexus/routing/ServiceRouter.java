package org.isolution.nexus.routing;

import org.isolution.nexus.domain.Endpoint;
import org.isolution.nexus.domain.Service;

/**
 * User: agwibowo
 * Date: 30/12/10
 * Time: 8:32 PM
 */
public interface ServiceRouter {

    Endpoint findSingleActiveEndpoint(String serviceURIString)
            throws ServiceNotFoundException, NoActiveRouteException;
}
