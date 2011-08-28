package org.isolution.nexus.domain;

import org.apache.commons.lang.StringUtils;

import javax.persistence.Embeddable;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * User: Alex Wibowo
 * Date: 26/01/11
 * Time: 10:26 AM
 */
@Embeddable
public class ServiceURI {

    /**
     * Namespace for the service
     */
    private String namespace;

    /**
     * Localname for the service
     */
    private String localName;

    public ServiceURI(String namespace, String localName) {
        validate(namespace, localName);
        this.namespace = namespace.trim();
        this.localName = localName.trim();
    }

    public ServiceURI() {
    }

    public void validate(String namespace, String localName) {
        if (isBlank(namespace)) {
            throw InvalidServiceURIException.invalidNamespace(namespace);
        }
        if (isBlank(localName)) {
            throw InvalidServiceURIException.invalidLocalname(localName);
        }
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public String toString() {
        return String.format("%1s:%2s", namespace, localName);
    }

    public String getServiceURIString() {
        return toString();
    }

    public static ServiceURI parse(String serviceURIString) {
        int separator = serviceURIString.lastIndexOf(":");
        if (separator < 0 ) {
            throw  InvalidServiceURIException.invalidServiceURIString(serviceURIString);
        }

        String namespace = StringUtils.substringBeforeLast(serviceURIString, ":");
        String localName = StringUtils.substringAfterLast(serviceURIString, ":");
        return new ServiceURI(namespace, localName);
    }
}
