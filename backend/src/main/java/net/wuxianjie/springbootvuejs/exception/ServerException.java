package net.wuxianjie.springbootvuejs.exception;

import net.wuxianjie.springbootvuejs.constants.RestCodeEnum;

/**
 * 服务器内部功能（一般为框架级）的异常
 *
 * @author 吴仙杰
 */
public class ServerException extends BaseException {

  public ServerException(String message, RestCodeEnum code) {
    super(message, code);
  }

  public ServerException(String message, Throwable cause,
      RestCodeEnum code) {
    super(message, cause, code);
  }
}
