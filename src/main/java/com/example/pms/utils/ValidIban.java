package com.example.pms.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IbanValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidIban {

    String message() default "Invalid IBAN";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
