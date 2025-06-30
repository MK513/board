package kkmm.back.board.domain.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kkmm.back.board.domain.validator.NotDefaultCategoryValidator;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = NotDefaultCategoryValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface NotDefaultCategory {

    String message() default "{NotDefaultCategory}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}