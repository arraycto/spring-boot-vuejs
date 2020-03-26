package net.wuxianjie.springbootvuejs.dto;

import net.wuxianjie.springbootvuejs.constants.ErrorCode;

public class RestResult<T> {

  private int error;
  private String message;
  private T result;

  public RestResult(T result) {
    this.error = ErrorCode.SUCCESS.getValue();
    this.message = ErrorCode.SUCCESS.getReasonPhrase();
    this.result = result;
  }

  public RestResult(int error, String message, T result) {
    this.error = error;
    this.message = message;
    this.result = result;
  }

  public int getError() {
    return error;
  }

  public void setError(int error) {
    this.error = error;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public T getResult() {
    return result;
  }

  public void setResult(T result) {
    this.result = result;
  }
}
