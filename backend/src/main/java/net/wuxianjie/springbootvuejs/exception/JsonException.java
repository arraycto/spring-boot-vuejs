package net.wuxianjie.springbootvuejs.exception;

import net.wuxianjie.springbootvuejs.rest.RestCodeEnum;

/**
 * JSON 解析失败的异常
 *
 * @author 吴仙杰
 */
@SuppressWarnings("unused")
public class JsonException extends BaseException {

  public JsonException(String message, RestCodeEnum code) {
    super(message, code);
  }

  public JsonException(String message, Throwable cause,
      RestCodeEnum code) {
    super(message, cause, code);
  }
}
