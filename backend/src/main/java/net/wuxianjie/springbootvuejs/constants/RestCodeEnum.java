package net.wuxianjie.springbootvuejs.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * HTTP API 错误码
 *
 * <p>HTTP 状态码说明：</p>
 *
 * <ul>
 *   <li>1xx 告知请求的处理进度和情况</li>
 *   <li>2xx 成功</li>
 *   <li>3xx 表示需要进一步操作</li>
 *   <li>4xx 客户端错误</li>
 *   <li>5xx 服务器错误</li>
 * </ul>
 *
 * <p>其中在 {@code 2xx} 范围内的 HTTP 状态码都代表服务器响应成功</p>
 *
 * <p>项目错误码说明：</p>
 *
 * <ul>
 *   <li>1000 ~ 1999：鉴权相关</li>
 *   <li>2000 ~ 2999：客户端请求相关</li>
 *   <li>3000 ~ 3999：程序内部运行相关</li>
 *   <li>4000 ~ 4999：调用外部服务相关</li>
 * </ul>
 *
 * @author 吴仙杰
 */
@AllArgsConstructor
@ToString
public enum RestCodeEnum {

  /**
   * 请求成功
   */
  SUCCESS(200, 0, "SUCCESS"),

  /**
   * 数据已经创建，无需重复提交
   */
  CREATED(201, 0, "Created"),

  /**
   * 没有内容可响应
   */
  NO_CONTENT(204, 0, "No Content"),

  /**
   * 客户端 ID 错误，比如：用户名不存在或错误、API Key 不正确等
   */
  INVALID_CLIENT_ID(401, 1001, "Unknown Client ID"),

  /**
   * 客户端密钥错误，比如：用户密码错误、Secret Key 不正确等
   */
  INVALID_CLIENT_SECRET(401, 1002, "Client Authentication Failed"),

  /**
   * Access token 未传入
   */
  MISSING_ACCESS_TOKEN(401, 1003, "Missing Access Token"),

  /**
   * Access token 已失效
   */
  INVALID_ACCESS_TOKEN(401, 1004, "Access Token Invalid Or No Longer Valid"),

  /**
   * Access token 已过期
   */
  EXPIRED_ACCESS_TOKEN(401, 1005, "Access Token Expired"),

  /**
   * 没有接口权限
   */
  NO_PERMISSION(403, 1006, "No Permission To Access Data"),

  /**
   * 未找所请求的服务
   */
  Not_Found(404, 2001, "No Message Available"),

  /**
   * HTTP 请求方法与服务端可接受的方法不一致
   */
  HTTP_REQUEST_METHOD_NOT_SUPPORTED(405, 2001, "HTTP Request Method Not Supported"),

  /**
   * HTTP 请求头 MIME 与服务端 MIME 不一致
   */
  HTTP_REQUEST_NOT_ACCEPTABLE(406, 2002, "HTTP Request Not Acceptable"),

  /**
   * 每天流量超限额
   */
  LIMITED_DAILY_REQUEST(429, 2101, "Open api Daily Request Limit Reached"),

  /**
   * QPS（Queries Per Second，每秒查询数）超限额
   */
  LIMITED_QPS(429, 2102, "Open API QPS Request Limit Reached"),

  /**
   * 请求总量超限额
   */
  LIMITED_TOTAL_REQUEST(429, 2103, "Open API Total Request Limit Reached"),

  /**
   * 必要参数未传入
   */
  MISSING_REQUIRED_PARAMETER(422, 2201, "Missing A Required Parameter"),

  /**
   * 参数格式错误
   */
  MALFORMED_PARAMETER(422, 2202, "Parameter Format Error"),

  /**
   * 程序内部运行出错
   */
  ERROR_SERVER(500, 3001, "Server Error"),

  /**
   * 当 HTTP 状态码还没有对应任意一个错误码时
   */
  NO_MAPPING_HTTP_STATUS(500, 3002, "No Mapping HTTP Status"),

  /**
   * 调用外部服务不可用
   */
  ERROR_INVOKE(503, 4001, "Invoke Error");

  /**
   * HTTP 状态码
   */
  @Getter
  private final int httpStatus;

  /**
   * 错误码
   */
  @Getter
  private final int errorCode;

  /**
   * 消息摘要
   */
  @Getter
  private final String message;

  /**
   * 返回指定数值的枚举常量
   *
   * @param httpStatus HTTP 状态码
   * @return 指定 HTTP 状态码的枚举常量，若有多个则随机返回一个
   * @throws IllegalArgumentException 当未找到与指定 HTTP 状态码相关匹配的枚举常量时
   */
  public static RestCodeEnum valueOf(int httpStatus) {
    RestCodeEnum codeEnum = resolve(httpStatus);
    if (codeEnum == null) {
      throw new IllegalArgumentException(String.format("未找到与 HTTP 状态码【%d】所匹配的错误码", httpStatus));
    }
    return codeEnum;
  }

  /**
   * 如果可能，将给定的 HTTP 状态代码解析为 {@code RestCodeEnum}
   *
   * @param httpStatus HTTP 状态代码（可能是非标准的）
   * @return 对应的 {@code RestCodeEnum}，或 {@code null}（如果没有找到）
   */
  public static RestCodeEnum resolve(int httpStatus) {
    for (RestCodeEnum item : values()) {
      if (item.httpStatus == httpStatus) {
        return item;
      }
    }
    return null;
  }
}
