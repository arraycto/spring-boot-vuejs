package net.wuxianjie.springbootvuejs.service;

import com.google.common.base.Joiner;
import com.google.common.cache.LoadingCache;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import net.wuxianjie.springbootvuejs.cache.CacheManager;
import net.wuxianjie.springbootvuejs.constants.RestCodeEnum;
import net.wuxianjie.springbootvuejs.dto.AccessTokenDto;
import net.wuxianjie.springbootvuejs.dto.PrincipalDto;
import net.wuxianjie.springbootvuejs.dto.TokenCacheDto;
import net.wuxianjie.springbootvuejs.exception.JwtAuthenticationException;
import net.wuxianjie.springbootvuejs.security.JwtManager;
import net.wuxianjie.springbootvuejs.security.SecurityConstants;
import net.wuxianjie.springbootvuejs.util.DateUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

/**
 * Access Token 服务
 *
 * @author 吴仙杰
 */
@Service
public class AccessTokenService {

  /**
   * 假定 access_token 有效期为 30 天
   */
  private static final int EXPIRES_DAYS = 30;

  private static final int USER_ID = 1;
  private static final String USER_NAME = "jason";
  private static final String PASSWORD = "123";

  /**
   * 获取 access token，有效期为 30 天
   *
   * @param userName 用户名
   * @param password 密码
   * @return 包含 access token 等信息的对象
   * @throws JwtAuthenticationException 当鉴权失败时
   */
  public AccessTokenDto getAccessToken(String userName, String password) {
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    PrincipalDto principal = new PrincipalDto();
    principal.setUserId(USER_ID);
    principal.setUserName(userName);

    boolean isNameRight = Objects.equals(USER_NAME, userName);
    boolean isPasswordRight = Objects.equals(PASSWORD, password);

    if (!isNameRight)
      throw new JwtAuthenticationException("用户名或密码错误", RestCodeEnum.INVALID_CLIENT_ID);

    if (!isPasswordRight)
      throw new JwtAuthenticationException("用户名或密码错误", RestCodeEnum.INVALID_CLIENT_SECRET);

    return generateAccessToken(principal, "admin,user,test");
  }

  /**
   * 刷新 {@code access_token}，有效期为 30 天
   *
   * <p>刷新后，旧的 access_token 将会被视为过期</p>
   *
   * @param principal Spring Security 中的身份验证对象
   * @param authorityList Spring Security 中的角色名列表
   * @return 包含 access token 等信息的对象
   */
  public AccessTokenDto refreshAccessToken(PrincipalDto principal, List<GrantedAuthority> authorityList) {
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    String userName = principal.getUserName();
    String roles = Joiner.on(",").join(authorityList);

    return generateAccessToken(principal, roles);
  }

  /**
   * 生成 access token
   *
   * @param principal 用户信息
   * @param roles    用户所拥有的角色名，以英文逗号分隔
   * @return access token
   */
  private AccessTokenDto generateAccessToken(PrincipalDto principal, String roles) {
    // 创建 JWT
    Date expirationTime = DateUtils.getAfterDaysDate(EXPIRES_DAYS);
    String accessToken = JwtManager
      .getInstance().generateAccessToken(expirationTime, principal.getUserName(), roles);

    // 加入缓存
    TokenCacheDto cache = new TokenCacheDto(principal.getUserId(), principal.getUserName(), accessToken);
    LoadingCache<String, Object> ttlCache = CacheManager.getInstance().getCache30DaysToLive();
    ttlCache.put(SecurityConstants.PREFIX_ACCESS_TOKEN_CACHE + principal.getUserName(), cache);

    return new AccessTokenDto(accessToken, expirationTime.getTime() / 1000);
  }
}
