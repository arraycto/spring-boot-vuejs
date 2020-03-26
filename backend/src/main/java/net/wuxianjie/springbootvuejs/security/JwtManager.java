package net.wuxianjie.springbootvuejs.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import java.util.Map;
import net.wuxianjie.springbootvuejs.exception.AuthenticationException;
import net.wuxianjie.springbootvuejs.rest.RestCodeEnum;
import net.wuxianjie.springbootvuejs.util.ApplicationContextHolder;

/**
 * JWT（JSON Web Token）管理器
 *
 * @author 吴仙杰
 */
@SuppressWarnings("unused")
public class JwtManager {

  /**
   * JWT 公开声明 - 用户名
   */
  public static final String JWT_PUBLIC_CLAIM_USER_NAME_KEY = "username";

  /**
   * JWT 公开声明 - 用户所拥有的角色名称，以英文逗号分隔
   */
  public static final String JWT_PUBLIC_CLAIM_ROLE_NAME_KEY = "role";

  /**
   * 可重用的算法实例，用于 JWT 签名和验证
   *
   * <p>选择 HMAC256 对称加密算法</p>
   */
  private Algorithm algorithm;
  /**
   * 可重用的验证器实例
   */
  private JWTVerifier jwtVerifier;

  /**
   * 将构造方法私有化，使外部无法直接实例化
   */
  private JwtManager() {
    // 选择 HMAC256 对称加密算法
    algorithm = ApplicationContextHolder.getBean("jwtAlgorithm", Algorithm.class);

    // 构建验证器实例
    jwtVerifier = JWT.require(algorithm).build();
  }

  /**
   * 单例模式：静态内部类法，延时加载，并且能保证线程安全
   *
   * <p>把 Singleton 实例放到一个静态内部类中，避免了静态实例在 Singleton 类加载的时候就创建对象</p>
   *
   * <p>由于静态内部类只会被加载一次，所以这种写法也是线程安全的</p>
   */
  private static class SingletonHolder {

    private static final JwtManager INSTANCE;

    static {
      INSTANCE = new JwtManager();
    }
  }

  /**
   * 获取对象单例
   */
  public static JwtManager getInstance() {
    return JwtManager.SingletonHolder.INSTANCE;
  }

  /**
   * 生成 JWT
   *
   * @param expirationTime 过期时间
   * @param username       用户名
   * @param roles          用户所拥有的角色名，以英文逗号分隔
   * @return JWT
   */
  public String generateAccessToken(Date expirationTime, String username, String roles) {
    return JWT.create()
      // 过期时间
      .withExpiresAt(expirationTime)
      .withClaim(JwtManager.JWT_PUBLIC_CLAIM_USER_NAME_KEY, username)
      .withClaim(JwtManager.JWT_PUBLIC_CLAIM_ROLE_NAME_KEY, roles)
      .sign(algorithm);
  }

  /**
   * 验证 JWT
   *
   * @param accessToken 需要验证的 token
   * @return 非 {@code null} 的 {@link Map}，包含了在令牌中定义的声明
   * @throws AuthenticationException 当 JWT 验证失败时抛出
   */
  public Map<String, Claim> verifyAccessToken(String accessToken) {
    DecodedJWT jwt;
    try {
      jwt = jwtVerifier.verify(accessToken);
    } catch (JWTVerificationException e) {
      throw new AuthenticationException(String.format("JWT【%s】验证失败：%s", accessToken, e.getMessage()), RestCodeEnum.ERROR);
    }
    return jwt.getClaims();
  }
}
