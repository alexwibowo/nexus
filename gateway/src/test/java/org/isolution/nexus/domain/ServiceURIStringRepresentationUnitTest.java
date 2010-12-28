package org.isolution.nexus.domain;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * User: agwibowo
 * Date: 26/12/10
 * Time: 3:49 PM
 */
public class ServiceURIStringRepresentationUnitTest {

    @Test
    public void should_get_represented_as_namespace_followed_by_localName() {
        ServiceURI serviceURI = new ServiceURI("namespace", "localName");
        assertThat(serviceURI.toString(), is("namespace:localName"));
    }

    @Test
    public void should_trim_whitespaces_in_namespace() throws Exception {
        ServiceURI serviceURI = new ServiceURI("    namespace   ", "localName");
        assertThat(serviceURI.toString(), is("namespace:localName"));
    }

    @Test
    public void should_trim_whitespaces_in_localName() throws Exception {
        ServiceURI serviceURI = new ServiceURI("namespace", "   localName  ");
        assertThat(serviceURI.toString(), is("namespace:localName"));
    }
}
