package com.devekoc.altaris.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomPhoneNumberValidator implements ConstraintValidator<ValidCustomPhoneNumber, String> {
    private final static String PHONE_PATTERN = "^6[0-9]{8}$";
    private Pattern pattern;

    @Override
    public void initialize(ValidCustomPhoneNumber constraintAnnotation) {
        pattern = Pattern.compile(PHONE_PATTERN);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
