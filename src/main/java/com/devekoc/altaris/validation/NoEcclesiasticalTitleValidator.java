package com.devekoc.altaris.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class NoEcclesiasticalTitleValidator implements ConstraintValidator<NoEcclesiasticalTitle, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        String regex = "(?i)\\b(Abbé|Abbe|Ab|Rev|Révérend|Father|Fr|Père|Pere|Frère|Frere|Sœur|Soeur|Sister|Sr|Monseigneur|Mgr|Cardinal|Évêque|Eveque)\\b";
        return !Pattern.compile(regex).matcher(value).find();
    }
}
