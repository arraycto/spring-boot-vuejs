package net.wuxianjie.springbootvuejs.exception;

import net.wuxianjie.springbootvuejs.rest.RestCodeEnum;

/**
 * 鉴权失败的异常
 *
 * @author 吴仙杰
 */
@SuppressWarnings("unused")
public class AuthenticationException extends BaseException {

  public AuthenticationException(String message, RestCodeEnum code) {
    super(message, code);
  }

  public AuthenticationException(String message, Throwable cause,
      RestCodeEnum code) {
    super(message, cause, code);
  }
}
