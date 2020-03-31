package net.wuxianjie.springbootvuejs.service;

import com.google.common.base.Joiner;
import com.google.common.cache.LoadingCache;
import java.util.Date;
import java.util.Objects;
import net.wuxianjie.springbootvuejs.cache.CacheManager;
import net.wuxianjie.springbootvuejs.constants.RestCodeEnum;
import net.wuxianjie.springbootvuejs.dto.AccessTokenDto;
import net.wuxianjie.springbootvuejs.exception.AuthenticationException;
import net.wuxianjie.springbootvuejs.security.JwtManager;
import net.wuxianjie.springbootvuejs.security.SecurityConstants;
import net.wuxianjie.springbootvuejs.util.DateUtils;
import org.springframework.security.core.Authentication;
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

  private static final String USER_NAME = "jason";
  private static final String PASSWORD = "123";

  /**
   * 获取 access token，有效期为 30 天
   *
   * @param userName 用户名
   * @param password 密码
   * @return 包含 access token 等信息的对象
   * @throws AuthenticationException 当鉴权失败时
   */
  public AccessTokenDto getAccessToken(String userName, String password) {
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    boolean isNameRight = Objects.equals(USER_NAME, userName);
    boolean isPasswordRight = Objects.equals(PASSWORD, password);

    if (isNameRight && isPasswordRight)
      return generateAccessToken(userName, "admin,user,test");

    if (!isNameRight)
      throw new AuthenticationException("用户名或密码错误", RestCodeEnum.INVALID_CLIENT_ID);

    throw new AuthenticationException("用户名或密码错误", RestCodeEnum.INVALID_CLIENT_SECRET);
  }

  /**
   * 刷新 {@code access_token}，有效期为 30 天
   *
   * <p>刷新后，旧的 access_token 将会被视为过期</p>
   *
   * @param authentication Spring Security 中的身份验证对象，由 Controller 方法自动注入
   * @return 包含 access token 等信息的对象
   */
  public AccessTokenDto refreshAccessToken(Authentication authentication) {
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    String userName = authentication.getName();
    String roles = Joiner.on(",").join(authentication.getAuthorities());

    return generateAccessToken(userName, roles);
  }

  /**
   * 生成 access token
   *
   * @param userName 用户名
   * @param roles    用户所拥有的角色名，以英文逗号分隔
   * @return access token
   */
  private AccessTokenDto generateAccessToken(String userName, String roles) {
    // 创建 JWT
    Date expirationTime = DateUtils.getAfterDaysDate(EXPIRES_DAYS);
    String accessToken = JwtManager
      .getInstance().generateAccessToken(expirationTime, userName, roles);

    // 加入缓存
    LoadingCache<String, Object> ttlCache = CacheManager.getInstance().getCache30DaysToLive();
    ttlCache.put(SecurityConstants.PREFIX_ACCESS_TOKEN_CACHE + userName, accessToken);

    return new AccessTokenDto(accessToken, expirationTime.getTime() / 1000);
  }
}
