package org.isolution.nexus.domain.dao.impl;

import org.isolution.nexus.domain.Endpoint;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * User: Alex Wibowo
 * Date: 18/05/11
 * Time: 9:50 PM
 */
public class HBEndpointDAOValidationUnitTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validation_should_fail_when_given_invalid_URI() {
        Endpoint endpoint = new Endpoint();
        endpoint.setUri("bogus uri");

        Set<ConstraintViolation<Endpoint>> constraintViolations = validator.validate(endpoint);
        assertThat(constraintViolations.isEmpty(), is(false));
        ConstraintViolation<Endpoint> violation = constraintViolations.iterator().next();
        assertThat(violation.getMessage(), is("Invalid URI"));
    }

    @Test
    public void validation_should_pass_when_given_valid_URI() {
        Endpoint endpoint = new Endpoint();
        endpoint.setUri("http://www.google.com");

        Set<ConstraintViolation<Endpoint>> constraintViolations = validator.validate(endpoint);
        assertThat(constraintViolations.isEmpty(), is(true));
    }
}
