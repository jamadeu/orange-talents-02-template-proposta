package br.com.zup.proposta.compartilhado.erros;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public class ErroDTO {

    @JsonProperty
    private Collection<String> mensagens;

    @JsonCreator
    public ErroDTO(Collection<String> mensagens) {
        this.mensagens = mensagens;
    }
}
