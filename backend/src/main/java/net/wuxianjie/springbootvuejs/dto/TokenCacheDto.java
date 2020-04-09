package net.wuxianjie.springbootvuejs.dto;

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
public class TokenCacheDto {

  private int userId;

  private String userName;

  private String accessToken;
}
