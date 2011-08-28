package org.isolution.nexus.routing;

import org.isolution.nexus.framework.NexusException;

import java.text.MessageFormat;

/**
 * User: Alex Wibowo
 * Date: 5/02/11
 * Time: 5:47 PM
 */
public class InactiveServiceException extends NexusException{

    private final String serviceURI;

    public InactiveServiceException(String serviceURI) {
        super(MessageFormat.format("Service [{0}] is not active", serviceURI));
        this.serviceURI = serviceURI;
    }

    public String getServiceURI() {
        return serviceURI;
    }
}
