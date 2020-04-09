package net.wuxianjie.springbootvuejs.security;

/**
 * 安全配置相关常量
 *
 * @author 吴仙杰
 */
public class SecurityConstants {

  /**
   * 不需要鉴权的请求地址
   */
  public static final String[] PERMIT_ALL_PATH = { "/api/token", "/static/**", "/index.html", "/" };

  /**
   * URL 中 token 的参数名
   */
  public static final String URL_PARAMETER_TOKEN = "access_token";

  /**
   * 缓存中保存了 {@code access_token} 的键名前缀
   */
  public static final String PREFIX_ACCESS_TOKEN_CACHE = "token_";

  /**
   * JWT 公开声明 - 用户名
   */
  public static final String JWT_PUBLIC_CLAIM_USER_NAME_KEY = "user_name";

  /**
   * JWT 公开声明 - 用户所拥有的角色名称，以英文逗号分隔
   */
  public static final String JWT_PUBLIC_CLAIM_ROLE_NAME_KEY = "roles";

  /**
   * Spring Security 要求角色名必须大写，且必须以前缀 {@code ROLE_} 开头
   */
  public static final String PREFIX_SPRING_SECURITY_ROLE_NAME = "ROLE_";
}
