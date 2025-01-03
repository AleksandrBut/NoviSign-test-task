package com.test.task.novisign.validation.annotation;

import com.test.task.novisign.validation.UrlValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UrlValidator.class})
public @interface ImageUrl {

    String message() default "com.test.task.novisign.validation.annotation.ImageUrl.message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
