package org.isolution.nexus.routing.impl;

import org.isolution.nexus.domain.*;
import org.isolution.nexus.domain.dao.EndpointDAO;
import org.isolution.nexus.domain.dao.ServiceDAO;
import org.isolution.nexus.routing.InactiveServiceException;
import org.isolution.nexus.routing.NoActiveRouteException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static java.util.Collections.reverse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * User: Alex Wibowo
 * Date: 8/01/11
 * Time: 2:30 AM
 */

public class DatabaseServiceRouterFindEndpointUnitTest {

    private DatabaseServiceRouter databaseServiceRouter;
    private Service service;

    @Mock
    private EndpointDAO mockEndpointDAO;

    @Mock
    private ServiceDAO mockServiceDAO;

    private ServiceURI serviceURI;
    private Endpoint activeEndpoint;
    private Endpoint inactiveEndpoint;

    @Before
    public void setup() {
        service = new Service();
        activeEndpoint = new Endpoint();
        inactiveEndpoint = new Endpoint();

        serviceURI = new ServiceURI("http://www.namespace.com/payment.xsd", "Payment");
        service.setServiceURI(serviceURI);

        service.addEndpoint(activeEndpoint, Status.ACTIVE);
        service.addEndpoint(inactiveEndpoint, Status.INACTIVE);

        initMocks(this);
        databaseServiceRouter = new DatabaseServiceRouter(mockServiceDAO, mockEndpointDAO);

        when(mockServiceDAO.getEndpointByServiceURI(anyString())).thenReturn(service);
    }

    @Test
    public void should_find_endoint_by_the_serviceURI() throws Exception{

        Endpoint endpoint = databaseServiceRouter.findSingleActiveEndpoint("http://www.namespace.com/payment.xsd:Payment");
        assertThat(endpoint, not(nullValue()));
        assertThat(endpoint, is(activeEndpoint));
    }

    @Test
    public void should_only_return_active_endpoint_by_the_serviceURI() throws Exception {
        reverse(service.getServiceEndpoints());
        assertThat(service.getServiceEndpoints().get(0).isActive(), is(false));
        assertThat(service.getServiceEndpoints().get(1).isActive(), is(true));

        Endpoint endpoint = databaseServiceRouter.findSingleActiveEndpoint("http://www.namespace.com/payment.xsd:Payment");
        assertThat(endpoint, not(nullValue()));
        assertThat(endpoint, is(activeEndpoint));
    }

    @Test(expected = InactiveServiceException.class)
    public void should_fail_when_service_is_inactive() throws Exception{
        service.inactivate();
        databaseServiceRouter.findSingleActiveEndpoint("http://www.namespace.com/payment.xsd:Payment");
    }

    @Test(expected = NoActiveRouteException.class)
    public void should_fail_when_the_endpoint_is_inactive() throws Exception{
        activeEndpoint.inactivate();
        inactiveEndpoint.inactivate();
        databaseServiceRouter.findSingleActiveEndpoint("http://www.namespace.com/payment.xsd:Payment");
    }

    @Test(expected = NoActiveRouteException.class)
    public void should_fail_when_the_relationship_is_inactive() throws Exception{
        for (ServiceEndpoint serviceEndpoint : service.getServiceEndpoints()) {
            serviceEndpoint.inactivate();
        }
        databaseServiceRouter.findSingleActiveEndpoint("http://www.namespace.com/payment.xsd:Payment");
    }
}
