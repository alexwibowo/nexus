package org.isolution.nexus.routing.impl;

import org.isolution.nexus.domain.Endpoint;
import org.isolution.nexus.domain.Service;
import org.isolution.nexus.domain.ServiceURI;
import org.isolution.nexus.domain.Status;
import org.isolution.nexus.domain.dao.EndpointDAO;
import org.isolution.nexus.domain.dao.ServiceDAO;
import org.isolution.nexus.routing.InactiveServiceException;
import org.isolution.nexus.routing.ServiceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * User: agwibowo
 * Date: 12/02/11
 * Time: 12:50 AM
 */
@RunWith(MockitoJUnitRunner.class)
public class DatabaseServiceRouterFindServiceUnitTest {

    @Mock
    private ServiceDAO mockServiceDAO;

    @Mock
    private EndpointDAO mockEndpointDAO;

    private DatabaseServiceRouter service;
    private Service activeService;
    private Service inactiveService;

    @Before
    public void setup() {
        service = new DatabaseServiceRouter(mockServiceDAO, mockEndpointDAO);
        activeService = new Service();
        activeService.setStatus(Status.ACTIVE);


        inactiveService = new Service();
        inactiveService.setStatus(Status.INACTIVE);

        when(mockServiceDAO.getEndpointByServiceURI("http://activeService"))
                .thenReturn(activeService);
        when(mockServiceDAO.getEndpointByServiceURI("http://inactiveService"))
                .thenReturn(inactiveService);
        when(mockServiceDAO.getEndpointByServiceURI("http://non-existing"))
                .thenThrow(new IncorrectResultSizeDataAccessException("non existing!",1));
    }

    @Test
    public void should_use_serviceDAO_to_find_the_exact_serviceURI() throws Exception {
        Service result = service.findActiveService("http://activeService");
        assertThat(result, not(nullValue()));
        verify(mockServiceDAO, times(1)).getEndpointByServiceURI("http://activeService");
    }

    @Test(expected = InactiveServiceException.class)
    public void should_throw_exception_when_service_is_found_but_inactive() throws Exception{
        service.findActiveService("http://inactiveService");
    }

    @Test(expected = ServiceNotFoundException.class)
    public void should_throw_exception_when_service_is_not_found() throws Exception {
        service.findActiveService("http://non-existing");
    }


}
