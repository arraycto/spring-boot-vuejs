package net.wuxianjie.springbootvuejs.exception;

import net.wuxianjie.springbootvuejs.rest.RestCodeEnum;

/**
 * Base64 处理失败的异常
 *
 * @author 吴仙杰
 */
public class Base64Exception extends BaseException {

  public Base64Exception(String message, RestCodeEnum code) {
    super(message, code);
  }

  public Base64Exception(String message, Throwable cause,
      RestCodeEnum code) {
    super(message, cause, code);
  }
}
