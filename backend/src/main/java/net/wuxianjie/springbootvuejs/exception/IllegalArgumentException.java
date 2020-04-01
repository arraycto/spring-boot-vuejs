package net.wuxianjie.springbootvuejs.exception;

import net.wuxianjie.springbootvuejs.constants.RestCodeEnum;

/**
 * 参数不合法时的异常
 *
 * @author 吴仙杰
 */
public class IllegalArgumentException extends BaseException {

  public IllegalArgumentException(String message, RestCodeEnum code) {
    super(message, code);
  }

  public IllegalArgumentException(String message, Throwable cause, RestCodeEnum code) {
    super(message, cause, code);
  }
}
