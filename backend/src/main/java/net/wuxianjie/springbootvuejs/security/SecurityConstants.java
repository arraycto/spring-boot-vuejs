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
  public static final String[] PERMIT_ALL_PATH = { "/api/token", };

  /**
   * URL 中 token 的参数名
   */
  public static final String URL_PARAMETER_TOKEN = "access_token";
}
