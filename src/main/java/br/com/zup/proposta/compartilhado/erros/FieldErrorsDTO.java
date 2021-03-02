package br.com.zup.proposta.compartilhado.erros;

public class FieldErrorsDTO {
    final String field;
    final String message;

    public FieldErrorsDTO(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
