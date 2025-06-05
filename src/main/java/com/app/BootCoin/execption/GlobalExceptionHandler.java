package com.app.BootCoin.execption;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<String> handleBusinessException(BusinessException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<String> handleEnumParseError(HttpMessageNotReadableException ex) {
    if (ex.getCause() instanceof InvalidFormatException) {
      return ResponseEntity.badRequest()
              .body("docType inválido. Valores permitidos: DNI, CEX, PASAPORTE");
    }
    return ResponseEntity.badRequest().body("Error en el formato del request.");
  }

}
