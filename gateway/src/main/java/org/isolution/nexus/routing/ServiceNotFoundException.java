package org.isolution.nexus.routing;

import org.isolution.nexus.framework.NexusException;

import java.text.MessageFormat;

/**
 * User: agwibowo
 * Date: 28/01/11
 * Time: 10:12 PM
 */
public class ServiceNotFoundException  extends NexusException {

    private String serviceURI;

    public ServiceNotFoundException(String serviceURI) {
        super(MessageFormat.format("Service [{0}] is not found in the registry", serviceURI));
        this.serviceURI = serviceURI;
    }

    public String getServiceURI() {
        return serviceURI;
    }
}
