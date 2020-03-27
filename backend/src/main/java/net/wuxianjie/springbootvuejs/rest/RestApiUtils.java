package net.wuxianjie.springbootvuejs.rest;

import net.wuxianjie.springbootvuejs.constants.RestCodeEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * RESTFul API 返回结果工具类
 *
 * @author 吴仙杰
 */
public class RestApiUtils {

  /**
   * 生成包含 HTTP 状态码和统一响应结果的最终控制器响应结果
   *
   * @param data 返回给客户端的结果数据
   * @return 最终控制器响应结果
   */
  public static <T> ResponseEntity<RestResultDto<T>> generateSuccess(T data) {
    RestResultDto<T> result = new RestResultDto<>(data);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  /**
   * 生成包含 HTTP 状态码和统一响应结果的最终控制器响应结果
   *
   * @param code    错误码
   * @param message 错误消息
   * @return 最终控制器响应结果
   */
  public static <T> ResponseEntity<RestResultDto<T>> generateError(RestCodeEnum code, String message) {
    RestResultDto<T> result = new RestResultDto<>(code, message, null);
    HttpStatus httpStatus = HttpStatus.resolve(code.getHttpStatus());
    return new ResponseEntity<>(result, httpStatus == null ? HttpStatus.INTERNAL_SERVER_ERROR : httpStatus);
  }
}
