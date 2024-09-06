package com.example.validation;

import com.example.validation.impl.ClientValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Кастомная аннотация, проверяющая существует ли клиент по заданному логину ИЛИ номеру телефона ИЛИ по email
 */
@Constraint(validatedBy = ClientValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClientExist {
    String message() default "login/email/phone is already registered";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
