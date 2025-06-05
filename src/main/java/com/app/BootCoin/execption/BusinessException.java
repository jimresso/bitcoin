package com.app.BootCoin.execption;

public class BusinessException extends RuntimeException {
  public BusinessException(String message) {
    super(message);
  }
}