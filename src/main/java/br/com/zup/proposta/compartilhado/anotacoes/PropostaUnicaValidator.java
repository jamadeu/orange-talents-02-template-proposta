package br.com.zup.proposta.compartilhado.anotacoes;

import br.com.zup.proposta.compartilhado.erros.PropostaUnicaException;
import br.com.zup.proposta.novaProposta.PropostaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PropostaUnicaValidator implements ConstraintValidator<PropostaUnica, String> {

    @Autowired
    private PropostaRepository propostaRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(propostaRepository.existsByDocumento(value)){
            throw new PropostaUnicaException("Ja existe uma proposta para este cliente");
        }
        return true;
    }
}
