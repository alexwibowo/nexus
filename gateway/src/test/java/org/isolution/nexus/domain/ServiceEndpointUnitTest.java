package org.isolution.nexus.domain;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * User: Alex Wibowo
 * Date: 28/01/11
 * Time: 10:25 PM
 */
public class ServiceEndpointUnitTest {

    private ServiceEndpoint se;
    private Endpoint endpoint;
    private Service service;

    @Before
    public void setup() {
        se = new ServiceEndpoint();
        service = new Service();
        endpoint = new Endpoint();
        se.setService(service);
        se.setEndpoint(endpoint);

        se.setStatus(Status.ACTIVE);
        endpoint.setStatus(Status.ACTIVE);
        se.setStatus(Status.ACTIVE);
    }

    @Test
    public void isRoutable_should_return_true_if_serviceEndpoint_is_active_and_endpoint_is_active() {
        assertThat(se.isRoutable(), is(true));
    }

    @Test
    public void isRoutable_should_return_false_if_serviceEndpoint_is_notActive() {
        se.setStatus(Status.INACTIVE);
        assertThat(se.isRoutable(), is(false));
    }

    @Test
    public void isRoutable_should_return_false_if_endpoint_is_notActive() {
        endpoint.setStatus(Status.INACTIVE);
        assertThat(se.isRoutable(), is(false));
    }

    @Test(expected = IllegalStateException.class)
    public void isRoutable_should_throw_exception_if_serviceEndpoint_doesnt_have_endpoint() {
        se.setEndpoint(null);
        se.isRoutable();
    }


}
