package org.isolution.nexus.domain;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * User: Alex Wibowo
 * Date: 30/04/11
 * Time: 7:23 PM
 */

public class EndpointUnitTest {

    @Test
    public void toURI_should_convert_endpoint_uri_string_to_URI_object() {
        Endpoint endpoint = new Endpoint();
        endpoint.setUri("http://www.google.com");

        assertThat(endpoint.toURI().toString(), is("http://www.google.com"));
    }

    @Test
    public void isActive_should_return_true_if_the_endpoint_has_ACTIVE_status() {
        Endpoint endpoint = new Endpoint();
        endpoint.setStatus(Status.ACTIVE);

        assertThat(endpoint.isActive(), is(true));
    }

    @Test
    public void isActive_should_return_false_if_the_endpoint_has_INACTIVE_status() {
        Endpoint endpoint = new Endpoint();
        endpoint.setStatus(Status.INACTIVE);

        assertThat(endpoint.isActive(), is(false));
    }

    @Test
    public void activate_should_set_status_to_ACTIVE() {
        Endpoint endpoint = new Endpoint();
        endpoint.activate();

        assertThat(endpoint.getStatus(), is(Status.ACTIVE));
    }

    @Test
    public void inactivate_should_set_status_to_INACTIVE() {
        Endpoint endpoint = new Endpoint();
        endpoint.deactivate();

        assertThat(endpoint.getStatus(), is(Status.INACTIVE));
    }


}
