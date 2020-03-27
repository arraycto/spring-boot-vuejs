package net.wuxianjie.springbootvuejs.exception;

import net.wuxianjie.springbootvuejs.rest.RestCodeEnum;

/**
 * HTTP 处理失败的异常
 *
 * @author 吴仙杰
 */
public class HttpException extends BaseException {

  public HttpException(String message, RestCodeEnum code) {
    super(message, code);
  }

  public HttpException(String message, Throwable cause,
      RestCodeEnum code) {
    super(message, cause, code);
  }
}
