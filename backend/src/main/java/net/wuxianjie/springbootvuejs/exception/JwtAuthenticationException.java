package net.wuxianjie.springbootvuejs.exception;

import net.wuxianjie.springbootvuejs.constants.RestCodeEnum;

/**
 * 鉴权失败的异常
 *
 * @author 吴仙杰
 */
public class JwtAuthenticationException extends BaseException {

  public JwtAuthenticationException(String message, RestCodeEnum code) {
    super(message, code);
  }

  public JwtAuthenticationException(String message, Throwable cause, RestCodeEnum code) {
    super(message, cause, code);
  }
}
