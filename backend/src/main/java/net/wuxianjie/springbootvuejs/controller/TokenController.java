package net.wuxianjie.springbootvuejs.controller;

import com.google.common.cache.LoadingCache;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import net.wuxianjie.springbootvuejs.cache.CacheManager;
import net.wuxianjie.springbootvuejs.dto.AccessTokenDto;
import net.wuxianjie.springbootvuejs.exception.AuthenticationException;
import net.wuxianjie.springbootvuejs.rest.RestCodeEnum;
import net.wuxianjie.springbootvuejs.rest.RestResult;
import net.wuxianjie.springbootvuejs.security.JwtManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TokenController {

  private final static String USER_NAME = "jason";
  private final static String PASSWORD = "123";

  @PostMapping("/token")
  public RestResult<AccessTokenDto> getAccessToken(@RequestParam("user_name") String userName, @RequestParam("password") String password) {
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    if (Objects.equals(USER_NAME, userName) && Objects.equals(PASSWORD, password)) {
      AccessTokenDto dto = this.generateAccessToken(userName, "ROLE_ADMIN");
      return new RestResult<>(dto);
    }

    throw new AuthenticationException("用户名或密码错误", RestCodeEnum.ERROR);
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
    Date expirationTime = getUtcExpiresInMilliseconds();
    String accessToken = JwtManager.getInstance().generateAccessToken(expirationTime, userName, roles);

    // 加入缓存
    LoadingCache<String, Object> ttlCache = CacheManager.getInstance().getCache30MinutesToLive();
    ttlCache.put(userName, accessToken);

    return new AccessTokenDto(accessToken, expirationTime.getTime() / 1000);
  }
  /**
   * 获取 30 分钟后的时间
   *
   * @return 30 分钟后的时间
   */
  private Date getUtcExpiresInMilliseconds() {
    Instant instant = Instant.now();
    Duration duration = Duration.ofMinutes(30);
    Instant instantMinutesLater = instant.plus(duration);
    return Date.from(instantMinutesLater);
  }
}
