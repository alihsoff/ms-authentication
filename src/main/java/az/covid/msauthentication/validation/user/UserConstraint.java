package az.covid.msauthentication.validation.user;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.TYPE})
@Constraint(validatedBy = UserValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserConstraint {
    String message() default "Account request validation error";

    Class[] groups() default {};

    Class[] payload() default {};

    String[] sortingFields() default {};
}
