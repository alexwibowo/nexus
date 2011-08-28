package org.isolution.nexus.routing;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * User: Alex Wibowo
 * Date: 29/04/11
 * Time: 12:26 AM
 */
public class ServiceNotFoundExceptionTest {

    @Test
    public void should_show_the_correct_error_message() {
        ServiceNotFoundException serviceNotFoundException = new ServiceNotFoundException("http://www.service.com:Service");
        assertThat(serviceNotFoundException.getMessage(), is("Service [http://www.service.com:Service] is not found in the registry"));
    }

    @Test
    public void should_be_able_to_handle_null_serviceURI() {
        ServiceNotFoundException serviceNotFoundException = new ServiceNotFoundException(null);
        assertThat(serviceNotFoundException.getMessage(), is("Service [null] is not found in the registry"));
    }
}
