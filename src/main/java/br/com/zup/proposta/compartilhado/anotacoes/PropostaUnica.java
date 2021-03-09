package br.com.zup.proposta.compartilhado.anotacoes;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Documented
@Constraint(validatedBy = {PropostaUnicaValidator.class})
@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PropostaUnica {

    String message() default "Ja existe uma proposta para este cliente";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
