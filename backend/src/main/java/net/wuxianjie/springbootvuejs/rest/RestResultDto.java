package net.wuxianjie.springbootvuejs.rest;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.wuxianjie.springbootvuejs.constants.RestCodeEnum;

/**
 * REST 统一结果对象
 *
 * <p>包装返回给客户端的结果对象</p>
 *
 * @author 吴仙杰
 */
@Data
@NoArgsConstructor
public class RestResultDto<T> {

  /**
   * HTTP 状态码 {@link RestCodeEnum#getHttpStatus()}
   */
  private int httpStatus;

  /**
   * 错误码 {@link RestCodeEnum#getErrorCode()}
   */
  private int errorCode;

  /**
   * 描述信息
   */
  private String message;

  /**
   * 具体业务的返回结果
   */
  private T result;

  /**
   * 构造统一结果对象（正确结果）
   *
   * @param result 具体业务的返回结果
   */
  public RestResultDto(T result) {
    this.httpStatus = RestCodeEnum.SUCCESS.getHttpStatus();
    this.errorCode = RestCodeEnum.SUCCESS.getErrorCode();
    this.message = RestCodeEnum.SUCCESS.getMessage();
    this.result = result;
  }

  /**
   * 构造统一结果对象
   *
   * @param code    错误码
   * @param message 错误信息
   * @param result  具体业务的返回结果
   */
  public RestResultDto(RestCodeEnum code, String message, T result) {
    this.httpStatus = code.getHttpStatus();
    this.errorCode = code.getErrorCode();
    this.message = message;
    this.result = result;
  }
}
