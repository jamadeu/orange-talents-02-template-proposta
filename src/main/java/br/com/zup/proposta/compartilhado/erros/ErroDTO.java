package br.com.zup.proposta.compartilhado.erros;

import java.util.Collection;

public class ErroDTO {
    private final Collection<String> mensagens;

    public ErroDTO(Collection<String> mensagens) {
        this.mensagens = mensagens;
    }

    public Collection<String> getMensagens() {
        return mensagens;
    }
}
