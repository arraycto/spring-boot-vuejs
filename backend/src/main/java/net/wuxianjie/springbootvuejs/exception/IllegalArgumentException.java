package net.wuxianjie.springbootvuejs.exception;

import lombok.Getter;
import net.wuxianjie.springbootvuejs.constants.RestCodeEnum;

/**
 * 参数不合法时的异常
 *
 * @author 吴仙杰
 */
public class IllegalArgumentException extends RuntimeException {

  @Getter
  private RestCodeEnum code;

  public IllegalArgumentException(String message, RestCodeEnum code) {
    super(message);
    this.code = code;
  }

  public IllegalArgumentException(String message, Throwable cause, RestCodeEnum code) {
    super(message, cause);
    this.code = code;
  }
}
