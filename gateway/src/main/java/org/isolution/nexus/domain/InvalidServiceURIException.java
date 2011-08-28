package org.isolution.nexus.domain;

import java.text.MessageFormat;

/**
 * User: Alex Wibowo
 * Date: 27/01/11
 * Time: 10:15 PM
 */
public class InvalidServiceURIException extends RuntimeException {

    public InvalidServiceURIException(String message) {
        super(message);
    }

    public static InvalidServiceURIException invalidNamespace(String namespace) {
        return new InvalidServiceURIException(MessageFormat.format("Invalid namespace: [{0}]", namespace));
    }

    public static InvalidServiceURIException invalidLocalname(String localName) {
        return new InvalidServiceURIException(MessageFormat.format("Invalid localName: [{0}]", localName));
    }

    public static InvalidServiceURIException invalidServiceURIString(String serviceURIString) {
        return new InvalidServiceURIException(MessageFormat.format("Invalid service URI: [{0}]", serviceURIString));
    }
}
