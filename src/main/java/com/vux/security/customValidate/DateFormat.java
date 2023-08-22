package com.vux.security.customValidate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateFormatValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateFormat {
    String message() default "Date must format by yyyy-MM-dd";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default  {};
}
