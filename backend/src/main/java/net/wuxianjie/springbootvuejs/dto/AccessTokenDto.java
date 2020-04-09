package net.wuxianjie.springbootvuejs.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Access token 数据传输对象
 *
 * @author 吴仙杰
 */
@Data
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AccessTokenDto {

  /**
   * 需要在 URL 中带上参数 {@code access_token}，才可以访问受保护的资源
   */
  private String accessToken;

  /**
   * 过期于，即在什么时间后 {@code access_token} 将过期（单位：秒）
   *
   * <p>一般为 30 天</p>
   */
  private long expiresIn;
}
