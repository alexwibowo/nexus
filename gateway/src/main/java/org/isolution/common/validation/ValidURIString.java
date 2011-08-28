package org.isolution.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * User: Alex Wibowo
 * Date: 18/05/11
 * Time: 9:30 PM
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidURIValidator.class)
@Documented
public @interface ValidURIString {

    String message() default "{org.isolution.nexus.domain.constraints.validuristring}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
