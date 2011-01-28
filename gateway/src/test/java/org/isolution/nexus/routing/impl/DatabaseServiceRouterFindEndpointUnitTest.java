package org.isolution.nexus.routing.impl;

import org.isolution.nexus.domain.Endpoint;
import org.isolution.nexus.domain.Service;
import org.isolution.nexus.domain.ServiceURI;
import org.isolution.nexus.domain.Status;
import org.isolution.nexus.domain.dao.EndpointDAO;
import org.isolution.nexus.domain.dao.ServiceDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * User: agwibowo
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

        service.setServiceURI(serviceURI);
        service.addEndpoint(activeEndpoint, Status.ACTIVE);
        service.addEndpoint(inactiveEndpoint, Status.INACTIVE);

        serviceURI = new ServiceURI("http://www.namespace.com/payment.xsd", "Payment");
        initMocks(this);
        databaseServiceRouter = new DatabaseServiceRouter(mockServiceDAO, mockEndpointDAO);
    }

    @Test
    public void should_find_endoint_by_the_serviceURI() throws Exception{
        when(mockServiceDAO.getEndpointByServiceURI(anyString())).thenReturn(service);

        Endpoint endpoint = databaseServiceRouter.findSingleActiveEndpoint("http://www.namespace.com/payment.xsd:Payment");
        assertThat(endpoint, not(nullValue()));
        assertThat(endpoint, is(activeEndpoint));
    }


    @Test
    public void should_fail_when_service_doesnt_have_namespace() {
        try {
            databaseServiceRouter.findSingleActiveEndpoint(":Payment");
            fail("Should have failed due to empty namespace");
        } catch (Exception e) {

        }
    }

    @Test
    public void should_fail_when_service_doesnt_have_localname() {
        try {
            databaseServiceRouter.findSingleActiveEndpoint("http://www.namespace.com/payment.xsd");
            fail("Should have failed due to empty localname");
        } catch (Exception e) {

        }
    }




}
