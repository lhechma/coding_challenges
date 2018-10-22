package com.n26.web.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = FreshnessValidator.class)
@Documented
public @interface Fresh {
    String message() default "${freshnessProperties.dateFormat}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
