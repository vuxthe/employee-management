package com.vux.security.customValidate;

import com.vux.security.customValidate.DateFormat;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateFormatValidator  implements ConstraintValidator<DateFormat, String> {
    final String pattern = "yyyy-MM-dd";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.length() != pattern.length())
            return false;
        return LocalDate.parse(s, formatter) != null;
    }
}
