package net.wuxianjie.springbootvuejs.config;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 网站相关配置
 *
 * @author 吴仙杰
 */
@Configuration
public class WebConfig {

  @Value("${jwt.secret}")
  private String jwtSecret;

  /**
   * 通过 Spring IOC 容器管理加密算法，该算法实例是可重用的，用于令牌签名和验证
   *
   * @return HMAC256 对称加密算法实例
   */
  @Bean
  public Algorithm jwtAlgorithm() {
    return Algorithm.HMAC256(jwtSecret);
  }
}
