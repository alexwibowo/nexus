package org.isolution.common.validation;

import org.apache.log4j.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.net.URL;

/**
 * User: agwibowo
 * Date: 18/05/11
 * Time: 9:34 PM
 */
public class ValidURIValidator implements ConstraintValidator<ValidURIString, String>{
    private static final Logger LOGGER = Logger.getLogger(ValidURIValidator.class.getName());

    private ValidURIString annotation;

    @Override
    public void initialize(ValidURIString constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            URL url = new URL(value);
            url.toURI();
            return true;
        } catch (Exception e) {
            String message = String.format("URL [%1s] is not supported, and cant be converted to URI", value);
            LOGGER.error(message, e);
            return false;
        }
    }
}
