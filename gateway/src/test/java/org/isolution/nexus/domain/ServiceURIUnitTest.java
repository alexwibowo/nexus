package org.isolution.nexus.domain;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * User: agwibowo
 * Date: 27/01/11
 * Time: 11:22 PM
 */
public class ServiceURIUnitTest {

    @Test
    public void toString_should_print_namespace_followed_by_localname() {
        ServiceURI serviceURI = new ServiceURI("http://www.namespace.com/namespace.xsd", "LocalName");
        assertThat(serviceURI.toString(), is("http://www.namespace.com/namespace.xsd:LocalName"));
    }

    @Test
    public void should_be_able_to_parse_from_proper_serviceURI_string() {
        ServiceURI parse = ServiceURI.parse("http://www.namespace.com/namespace.xsd:LocalName");
        assertThat(parse.getNamespace(), is("http://www.namespace.com/namespace.xsd"));
        assertThat(parse.getLocalName(), is("LocalName"));
    }

    @Test
    public void should_fail_to_parse_serviceURI_without_namespace() {

    }

    @Test
    public void should_fail_to_parse_serviceURI_without_localName() {

    }

    @Test
    public void should_fail_on_parsing_serviceURI_with_blank_namespace() {

    }

    @Test
    public void should_fail_on_parsing_serviceURI_with_blank_localName() {

    }
}
