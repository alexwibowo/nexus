package org.isolution.nexus.domain;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * User: agwibowo
 * Date: 21/12/10
 * Time: 12:08 AM
 */
public class ServiceURI extends AbstractModel{
    /**
     * Namespace for the service
     */
    private final String namespace;

    /**
     * Localname for the service
     */
    private final String localName;

    public ServiceURI(String namespace, String localName) {
        validate(namespace, localName);
        this.namespace = namespace.trim();
        this.localName = localName.trim();
    }

    public void validate(String namespace, String localName) {
        checkArgument(!isBlank(namespace), format("Invalid namespace: [%s]", namespace));
        checkArgument(!isBlank(localName), format("Invalid localName: [%s]", localName));
    }

    public String getNamespace() {
        return namespace;
    }

    public String getLocalName() {
        return localName;
    }

    @Override
    public String toString() {
        return format("%1s:%2s", namespace, localName);
    }
}
