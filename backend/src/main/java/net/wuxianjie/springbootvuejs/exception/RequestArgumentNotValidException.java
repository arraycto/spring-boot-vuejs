package net.wuxianjie.springbootvuejs.exception;

import net.wuxianjie.springbootvuejs.constants.RestCodeEnum;

/**
 * 客户端请求参数不合法时的异常
 *
 * <p>该异常作为对前端参数校验的补充，仅以 warn 日志级别记录参数不合法信息</p>
 *
 * @author 吴仙杰
 */
public class RequestArgumentNotValidException extends BaseException {

  public RequestArgumentNotValidException(String message, RestCodeEnum code) {
    super(message, code);
  }

  public RequestArgumentNotValidException(String message, Throwable cause, RestCodeEnum code) {
    super(message, cause, code);
  }
}
