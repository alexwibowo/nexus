package org.isolution.nexus.domain;

/**
 * User: Alex Wibowo
 * Date: 27/01/11
 * Time: 10:02 PM
 */
public class ServiceURITestFixture {

    public static ServiceURI noNamespace() {
        return new ServiceURIBuilder().withNamespace("").build();
    }

    public static ServiceURI noLocalName() {
        return new ServiceURIBuilder().withLocalName("").build();
    }

    public static ServiceURI getDefault() {
        return new ServiceURIBuilder().build();
    }

    public static ServiceURI getNotRegisteredServiceURI() {
        return new ServiceURIBuilder().withNamespace("http://www.unregistered.com").withLocalName("unregistered").build();
    }
}
