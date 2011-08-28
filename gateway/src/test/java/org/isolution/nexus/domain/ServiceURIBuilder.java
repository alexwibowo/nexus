package org.isolution.nexus.domain;

/**
 * User: Alex Wibowo
 * Date: 27/01/11
 * Time: 9:57 PM
 */
public class ServiceURIBuilder {

    private ServiceURI model;

    public ServiceURIBuilder() {
        model = new ServiceURI("http://www.default.com/default.xsd", "DefaultReq");
    }

    public ServiceURIBuilder withLocalName(String value) {
        model.setLocalName(value);
        return this;
    }

    public ServiceURIBuilder withNamespace(String value) {
        model.setNamespace(value);
        return this;
    }

    public ServiceURI build() {
        ServiceURI returned = model;
        model = null;
        return returned;
    }


}
