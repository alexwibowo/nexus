package org.isolution.nexus.domain;

import org.apache.commons.lang.NotImplementedException;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * User: Alex Wibowo
 * Date: 5/02/11
 * Time: 6:29 PM
 */
public class ServiceUnitTest {

    private Service service;

    @Before
      public void setup() {
        service = new Service();
        service.setStatus(Status.ACTIVE);

        Endpoint endpoint = new Endpoint();
        endpoint.activate();
        endpoint.setUri("endpoint1");
        service.addEndpoint(endpoint, Status.ACTIVE);


        Endpoint endpoint1 = new Endpoint();
        endpoint1.activate();
        endpoint1.setUri("endpoint2");
        service.addEndpoint(endpoint1, Status.ACTIVE);
    }

    @Test
    public void should_set_all_serviceEndpoint_relationship_to_inactive_when_inactivating_service() {
        service.inactivate();
        assertThat(service.isActive(), is(false));
        assertThat(service.getServiceEndpoints().get(0).isActive(), is(false));
        assertThat(service.getServiceEndpoints().get(1).isActive(), is(false));
    }

    @Test
    public void should_change_status_to_active_on_activate() {
       service.setStatus(Status.INACTIVE);
       service.activate();
        assertThat(service.isActive(), is(true));
    }

    @Test
    public void should_not_change_serviceEndpoint_status_to_active_on_activate() {
        service.getServiceEndpoints().get(0).inactivate();
        service.getServiceEndpoints().get(1).inactivate();
        service.setStatus(Status.INACTIVE);
        service.activate();
        assertThat(service.getServiceEndpoints().get(0).isActive(), is(false));
        assertThat(service.getServiceEndpoints().get(1).isActive(), is(false));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void should_not_allow_modification_to_endpoint_collection_returned_from_service() {
        service.getEndpoints().add(new Endpoint());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void should_not_allow_modificaiton_to_active_endpoint_collection_returned_from_service() {
        service.getActiveEndpoints().add(new Endpoint());
    }

    @Test
    public void isActive_should_return_true_when_service_has_active_status() {
       service.setStatus(Status.ACTIVE);
        assertThat(service.isActive(), is(true));
    }

    @Test
    public void isActive_should_return_false_when_service_has_inactive_status() {
        service.setStatus(Status.INACTIVE);
        assertThat(service.isActive(), is(false));
    }


   }
