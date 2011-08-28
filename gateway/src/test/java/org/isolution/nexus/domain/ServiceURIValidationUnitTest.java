package org.isolution.nexus.domain;

import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * User: Alex Wibowo
 * Date: 24/12/10
 * Time: 3:04 PM
 */
public class ServiceURIValidationUnitTest {

    @Test
    public void should_pass_when_both_namespace_and_localName_are_not_blank() throws Exception {
        new Service("a namespace", "a localName");
    }

    @Test
    public void should_fail_when_namespace_is_null(){
        try {
            new Service(null, "a localName");
            fail("Should have failed, due to null namespace");
        } catch (InvalidServiceURIException e) {
            assertThat(e.getMessage(), containsString("Invalid namespace: [null]"));
        }
    }

    @Test
    public void should_fail_when_namespace_is_blank() {
        try {
            new Service(" ", "a localName");
            fail("Should have failed, due to blank namespace");
        } catch (InvalidServiceURIException e) {
            assertThat(e.getMessage(), containsString("Invalid namespace: [ ]"));
        }
    }

    @Test
    public void should_fail_when_localName_is_null() {
        try {
            new Service("a namespace", null);
            fail("Should have failed, due to null localName");
        } catch (InvalidServiceURIException e) {
            assertThat(e.getMessage(), containsString("Invalid localName: [null]"));
        }
    }

    @Test
    public void should_fail_when_localName_is_blank() {
        try {
            new Service("a namespace", " ");
            fail("Should have failed, due to blank localName");
        } catch (InvalidServiceURIException e) {
            assertThat(e.getMessage(), containsString("Invalid localName: [ ]"));
        }
    }
}
