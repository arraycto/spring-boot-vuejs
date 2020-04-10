package net.wuxianjie.springbootvuejs.controller;

import java.util.List;
import net.wuxianjie.springbootvuejs.dto.PrincipalDto;
import net.wuxianjie.springbootvuejs.service.AccessTokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Access Token 控制器
 *
 * @author 吴仙杰
 */
@RestController
@RequestMapping("/token")
public class AccessTokenController {

  private final AccessTokenService service;

  public AccessTokenController(AccessTokenService service) {
    this.service = service;
  }

  /**
   * 获取 {@code access_token}，一般有效期为 30 天
   *
   * <p>调用 API 时必须在 URL 中带上 {@code access_token} 参数</p>
   *
   * @param userName 用户名
   * @param password 密码
   * @return 包含 access token 等信息的对象
   */
  @PostMapping()
  public Object accessToken(@RequestParam("user_name") String userName, @RequestParam("password") String password) {
    userName = userName.trim();
    password = password.trim();
    return service.getAccessToken(userName, password);
  }

  /**
   * 刷新 {@code access_token}，一般有效期为 30 天
   *
   * <p>刷新后，旧的 {@code access_token} 将会被视为过期</p>
   *
   * <p>调用 API 时必须在 URL 中带上 {@code access_token} 参数</p>
   *
   * @return 包含 access token 等信息的对象
   */
  @SuppressWarnings("unchecked")
  @GetMapping("/refresh")
  public Object refreshing(Authentication authentication) {

    PrincipalDto principal = (PrincipalDto) authentication.getPrincipal();
    List<GrantedAuthority> authorityList = (List<GrantedAuthority>) authentication.getAuthorities();

    return service.refreshAccessToken(principal, authorityList);
  }
}
