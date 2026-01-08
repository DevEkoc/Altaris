package com.devekoc.altaris.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NoEcclesiasticalTitleValidator.class)
public @interface NoEcclesiasticalTitle {
    String message() default "Le nom ne doit pas contenir de titre honorifique ou ecclésial";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
