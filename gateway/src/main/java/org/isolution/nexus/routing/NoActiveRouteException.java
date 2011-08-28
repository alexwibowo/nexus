package org.isolution.nexus.routing;

import org.isolution.nexus.domain.Service;
import org.isolution.nexus.framework.NexusException;

import java.text.MessageFormat;

/**
 * User: Alex Wibowo
 * Date: 28/01/11
 * Time: 10:17 PM
 */
public class NoActiveRouteException extends NexusException {

    public NoActiveRouteException(String message) {
        super(message);
    }

    public static NoActiveRouteException noActiveEndpointForService(Service service) {
        return new NoActiveRouteException(MessageFormat.format("There are no active endpoints for the given service [{0}]", service.toString()));
    }
}
