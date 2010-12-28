package org.isolution.nexus.domain;

import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * User: agwibowo
 * Date: 24/12/10
 * Time: 3:04 PM
 */
public class ServiceURIValidationUnitTest {

    @Test
    public void should_pass_when_both_namespace_and_localName_are_not_blank() throws Exception {
        new ServiceURI("a namespace", "a localName");
    }

    @Test
    public void should_fail_when_namespace_is_null(){
        try {
            new ServiceURI(null, "a localName");
            fail("Should have failed, due to null namespace");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), containsString("Invalid namespace: [null]"));
        }
    }

    @Test
    public void should_fail_when_namespace_is_blank() {
        try {
            new ServiceURI(" ", "a localName");
            fail("Should have failed, due to blank namespace");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), containsString("Invalid namespace: [ ]"));
        }
    }

    @Test
    public void should_fail_when_localName_is_null() {
        try {
            new ServiceURI("a namespace", null);
            fail("Should have failed, due to null localName");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), containsString("Invalid localName: [null]"));
        }
    }

    @Test
    public void should_fail_when_localName_is_blank() {
        try {
            new ServiceURI("a namespace", " ");
            fail("Should have failed, due to blank localName");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), containsString("Invalid localName: [ ]"));
        }
    }
}
