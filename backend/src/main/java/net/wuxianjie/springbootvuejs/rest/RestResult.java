package net.wuxianjie.springbootvuejs.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * REST 统一结果对象
 *
 * <p>包装返回给客户端的结果对象</p>
 *
 * @author 吴仙杰
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestResult<T> {

  /**
   * 状态码 {@link RestCodeEnum}
   */
  private String status;

  /**
   * 描述信息
   */
  private String message;

  /**
   * 具体业务的返回结果
   */
  private T result;

  /**
   * 操作成功（设置默认成功的错误码和描述信息），只需设置具体业务的返回结果
   *
   * @param result 具体业务的返回结果
   */
  public RestResult(T result) {
    this.status = RestCodeEnum.SUCCESS.getStatus();
    this.message = RestCodeEnum.SUCCESS.getMessage();
    this.result = result;
  }

  /**
   * 只设置状态码和描述信息，不需要具体业务的返回结果
   *
   * @param restCode 状态码，详见 {@link RestCodeEnum}
   * @param message  描述信息
   */
  public RestResult(RestCodeEnum restCode, String message) {
    this.status = restCode.getStatus();
    this.message = message;
  }
}
