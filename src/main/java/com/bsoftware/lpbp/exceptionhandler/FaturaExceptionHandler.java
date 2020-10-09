package com.bsoftware.lpbp.exceptionhandler;

import com.bsoftware.lpbp.service.exeption.UsuarioException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class FaturaExceptionHandler extends ResponseEntityExceptionHandler {
    private final MessageSource messageSource;

    public FaturaExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        String mensagemUsuario = messageSource.getMessage("menssagem.invalida", null, LocaleContextHolder.getLocale());
        String mensagemDoDesenvolvedor = ex.getCause() == null ? ex.toString() : ex.getCause().toString();
        return handleExceptionInternal(ex, new Erro(mensagemUsuario, mensagemDoDesenvolvedor), headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        List<Erro> erros = criarListaDeErros(ex.getBindingResult());
        return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
    }

    private List<Erro> criarListaDeErros(BindingResult bindingResult) {
        List<Erro> erros = new ArrayList<>();
        for (FieldError fieldeerror : bindingResult.getFieldErrors()) {
            String mensagemUsuario = messageSource.getMessage(fieldeerror, LocaleContextHolder.getLocale());
            String mensagemDoDesenvolvedor = fieldeerror.toString();
            erros.add(new Erro(mensagemUsuario, mensagemDoDesenvolvedor));
        }
        return erros;
    }

    @ExceptionHandler({EmptyResultDataAccessException.class})
    public ResponseEntity<Object> handlerEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request) {
        String mensagemUsuario = messageSource.getMessage("recurso.nao-encontrado", null, LocaleContextHolder.getLocale());
        String mensagemDoDesenvolvedor = ex.toString();
        List<Erro> erros = Collections.singletonList(new Erro(mensagemUsuario, mensagemDoDesenvolvedor));
        return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handlerDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        String mensagemUsuario = messageSource.getMessage("recurso.operacao-nao-permitida", null, LocaleContextHolder.getLocale());
        String mensagemDoDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);
        List<Erro> erros = Collections.singletonList(new Erro(mensagemUsuario, mensagemDoDesenvolvedor));
        return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({UsuarioException.class})
    public ResponseEntity<Object> handlerUsuarioException(UsuarioException ex, WebRequest request) {
        String mensagemUsuario = ex.getMessage();
        String mensagemDoDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);
        List<Erro> erros = Collections.singletonList(new Erro(mensagemUsuario, mensagemDoDesenvolvedor));
        return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Getter
    @Setter
    public static class Erro {
        private String mensagemUsuario;
        private String mensagemDoDesenvolvedor;

        public Erro(String mensagemUsuario, String mensagemDoDesenvolvedor) {
            this.mensagemUsuario = mensagemUsuario;
            this.mensagemDoDesenvolvedor = mensagemDoDesenvolvedor;
        }
    }
}
