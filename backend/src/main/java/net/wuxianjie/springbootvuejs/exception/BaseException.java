package net.wuxianjie.springbootvuejs.exception;

import lombok.Getter;
import net.wuxianjie.springbootvuejs.constants.RestCodeEnum;

/**
 * 自定义运行时异常的基类
 *
 * @author 吴仙杰
 */
public class BaseException extends RuntimeException {

  @Getter
  private RestCodeEnum code;

  public BaseException(String message, RestCodeEnum code) {
    super(message);
    this.code = code;
  }

  public BaseException(String message, Throwable cause,
      RestCodeEnum code) {
    super(message, cause);
    this.code = code;
  }
}
