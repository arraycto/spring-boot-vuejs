package net.wuxianjie.springbootvuejs.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * HTTP API 状态码
 *
 * @author 吴仙杰
 */
@AllArgsConstructor
@ToString
public enum RestCodeEnum {

  SUCCESS("success", "操作成功"),
  ERROR("error", "操作失败");

  /**
   * 错误码
   */
  @Getter
  private final String status;

  /**
   * 提示信息
   */
  @Getter
  private final String message;
}
