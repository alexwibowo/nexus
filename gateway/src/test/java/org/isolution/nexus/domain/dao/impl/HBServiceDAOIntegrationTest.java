package org.isolution.nexus.domain.dao.impl;

import org.isolution.nexus.domain.*;
import org.isolution.nexus.domain.dao.ServiceDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * User: agwibowo
 * Date: 27/01/11
 * Time: 5:36 PM
 */
@ContextConfiguration(locations = {"classpath:spring-persistence.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class HBServiceDAOIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests{

    @Autowired
    @Qualifier("hibernate")
    private ServiceDAO serviceDAO;

    private Service defaultActiveService;

    @Before
    public void setup() {
        defaultActiveService = new Service();
        defaultActiveService.setServiceURI(ServiceURITestFixture.getDefault());
        defaultActiveService.setStatus(Status.ACTIVE);
        defaultActiveService = serviceDAO.save(defaultActiveService);
    }

    @Test
    public void getEndpointByServiceURI_should_be_able_to_find_service_by_serviceURI() throws Exception {
        ServiceURI serviceURI = ServiceURITestFixture.getDefault();

        Service service = serviceDAO.getEndpointByServiceURI(serviceURI.toString());
        assertThat(service, not(nullValue()));
        assertThat(service.getServiceURI().getLocalName(), is(serviceURI.getLocalName()));
        assertThat(service.getServiceURI().getNamespace(), is(serviceURI.getNamespace()));
    }

    @Test(expected = InvalidServiceURIException.class)
    public void getEndpointByServiceURI_should_fail_when_given_invalid_serviceURI() throws Exception {
            serviceDAO.getEndpointByServiceURI(ServiceURITestFixture.noLocalName().toString());
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getEndpointByServiceURI_should_fail_when_given_serviceURI_that_is_not_registered() throws Exception {
        serviceDAO.getEndpointByServiceURI(ServiceURITestFixture.getNotRegisteredServiceURI().toString());
    }

}
