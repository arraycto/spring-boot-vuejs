package net.wuxianjie.springbootvuejs.controller;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import net.wuxianjie.springbootvuejs.constants.ErrorCode;
import net.wuxianjie.springbootvuejs.dto.AccessToken;
import net.wuxianjie.springbootvuejs.dto.RestResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AccessTokenController {

  private final static String USER_NAME = "jason";
  private final static String PASSWORD = "123";
  private final static String ACCESS_TOKEN = "25.b55fe1d287227ca97aab219bb249b8ab.315360000.1798284651.282335-8574074";

  @PostMapping("/access_token")
  public RestResult<AccessToken> getAccessToken(
      @RequestParam("userName") String userName,
      @RequestParam("password") String password) {

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    if (Objects.equals(USER_NAME, userName)
        && Objects.equals(PASSWORD, password)) {
      return new RestResult<>(new AccessToken(ACCESS_TOKEN, getUtcExpiresInMilliseconds()));
    }

    return new RestResult<>(ErrorCode.USER_NAME_OR_PASSWORD_ERROR.getValue(), "意料之中的认证失败", null);
  }

  /**
   * 获取 30 分钟后的 UTC 毫秒值
   *
   * @return UTC 毫秒值
   */
  private long getUtcExpiresInMilliseconds() {
    Instant instant = Instant.now();
    Duration duration = Duration.ofMinutes(30);
    Instant instantMinutesLater = instant.plus(duration);
    return instantMinutesLater.toEpochMilli();
  }
}
