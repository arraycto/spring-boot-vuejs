package net.wuxianjie.springbootvuejs.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.wuxianjie.springbootvuejs.cache.CacheManager;

/**
 * {@link CacheManager} 中 key（{@code token_用户名}）所对应的值
 *
 * @author 吴仙杰
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TokenCacheDto {

  private int userId;

  private String userName;

  private String accessToken;
}
