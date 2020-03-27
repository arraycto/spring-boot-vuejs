package net.wuxianjie.springbootvuejs.controller;

import com.google.common.cache.LoadingCache;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import net.wuxianjie.springbootvuejs.cache.CacheManager;
import net.wuxianjie.springbootvuejs.dto.AccessTokenDto;
import net.wuxianjie.springbootvuejs.exception.AuthenticationException;
import net.wuxianjie.springbootvuejs.constants.RestCodeEnum;
import net.wuxianjie.springbootvuejs.security.JwtManager;
import net.wuxianjie.springbootvuejs.util.DateUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * TOKEN 管理控制器
 *
 * @author 吴仙杰
 */
@RestController
@RequestMapping("/api")
public class TokenController {

  /**
   * 假定 access_token 有效期为 30 分钟
   */
  private static final int EXPIRES_MINUTES = 30;

  private static final String USER_NAME = "jason";
  private static final String PASSWORD = "123";

  /**
   * 获取 {@code access_token}，一般有效期为 30 分钟
   *
   * <p>调用 API 时必须在 URL 中带上 {@code access_token} 参数</p>
   *
   * @param userName 用户名
   * @param password 密码
   * @return 鉴权结果
   */
  @PostMapping("/token")
  public Object getAccessToken(@RequestParam("user_name") String userName, @RequestParam("password") String password) {
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    boolean isNameRight = Objects.equals(USER_NAME, userName);
    boolean isPasswordRight = Objects.equals(PASSWORD, password);

    if (isNameRight && isPasswordRight)
      return generateAccessToken(userName, "ROLE_ADMIN");

    if (!isNameRight)
      throw new AuthenticationException("用户名或密码错误", RestCodeEnum.INVALID_CLIENT_ID);

    throw new AuthenticationException("用户名或密码错误", RestCodeEnum.INVALID_CLIENT_SECRET);
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
    Date expirationTime = DateUtils.getAfterMinutesDate(EXPIRES_MINUTES);
    String accessToken = JwtManager.getInstance().generateAccessToken(expirationTime, userName, roles);

    // 加入缓存
    LoadingCache<String, Object> ttlCache = CacheManager.getInstance().getCache30MinutesToLive();
    ttlCache.put(userName, accessToken);

    return new AccessTokenDto(accessToken, expirationTime.getTime() / 1000);
  }
}
