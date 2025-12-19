package com.devekoc.altaris.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CustomPhoneNumberValidator.class)
@Documented
public @interface ValidCustomPhoneNumber {
    String message() default "Le numéro de téléphone doit contenir exactement 9 chiffres commençant par un 6 !";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
