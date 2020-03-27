package net.wuxianjie.springbootvuejs.exception;

import net.wuxianjie.springbootvuejs.constants.RestCodeEnum;

/**
 * 日期处理失败的异常
 *
 * @author 吴仙杰
 */
public class DateException extends BaseException {

  public DateException(String message, RestCodeEnum code) {
    super(message, code);
  }

  public DateException(String message, Throwable cause,
      RestCodeEnum code) {
    super(message, cause, code);
  }
}
