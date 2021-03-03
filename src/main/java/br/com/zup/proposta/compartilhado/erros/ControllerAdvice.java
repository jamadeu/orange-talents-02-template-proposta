package br.com.zup.proposta.compartilhado.erros;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestControllerAdvice
public class ControllerAdvice {

    final Logger logger = LoggerFactory.getLogger(ControllerAdvice.class);

    final MessageSource messageSource;

    public ControllerAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<FieldErrorsDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<FieldErrorsDTO> dto = new ArrayList<>();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        fieldErrors.forEach(e -> {
            String message = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            FieldErrorsDTO error = new FieldErrorsDTO(e.getField(), message);
            dto.add(error);
            logger.error("Campo {}, mensagem={}", e.getField(), message);
        });
        return dto;
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErroDTO> handleResponseStatusException(ResponseStatusException exception) {
        Collection<String> mensagens = new ArrayList<>();
        mensagens.add(exception.getReason());
        ErroDTO erroDTO = new ErroDTO(mensagens);
        logger.error("Status {}, mensagem = {}", exception.getStatus(), exception.getReason());
        return ResponseEntity.status(exception.getStatus()).body(erroDTO);
    }
}
