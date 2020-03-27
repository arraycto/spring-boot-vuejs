package net.wuxianjie.springbootvuejs.security;

import com.auth0.jwt.interfaces.Claim;
import com.google.common.base.Strings;
import com.google.common.cache.LoadingCache;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.wuxianjie.springbootvuejs.cache.CacheManager;
import net.wuxianjie.springbootvuejs.exception.AuthenticationException;
import net.wuxianjie.springbootvuejs.rest.RestCodeEnum;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 重写 Spring Security 认证过滤器
 *
 * @author 吴仙杰
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    if (!isNeedAuthentication(request)) {
      chain.doFilter(request, response);
      return;
    }

    UsernamePasswordAuthenticationToken authResult = authentication(request);

    // 向 Spring Security 上下文中保存信息
    SecurityContextHolder.getContext().setAuthentication(authResult);

    chain.doFilter(req, res);
  }

  /**
   * 判断当前请求是否需要鉴权
   *
   * @param request HTTP 请求对象
   * @return 若需要鉴权，则返回 {@code true}；否则返回 {@code false}
   */
  private boolean isNeedAuthentication(HttpServletRequest request) {
    for (String item : SecurityConstants.PERMIT_ALL_PATH) {
      AntPathRequestMatcher antPathRequestMatcher = new AntPathRequestMatcher(item);
      boolean isMatch = antPathRequestMatcher.matches(request);
      if (isMatch) {
        return false;
      }
    }
    return true;
  }

  /**
   * 鉴权操作
   *
   *
   * @param request HTTP 请求对象
   * @return 表示验证通过后，包含用户名、密码和角色的对象
   */
  private UsernamePasswordAuthenticationToken authentication(HttpServletRequest request) {
    // 1、验证 HTTP URL 中是否存在 `access_token`
    String accessToken = request.getParameter(SecurityConstants.URL_PARAMETER_TOKEN);
    if (Strings.isNullOrEmpty(accessToken))
      throw new AuthenticationException("【access_token】缺失", RestCodeEnum.ERROR);

    // 2、验证 access token，并获取 JWT 中的声明
    Map<String, Claim> claimMap;
    try {
      claimMap = JwtManager.getInstance().verifyAccessToken(accessToken);
    } catch (AuthenticationException e) {
      throw new AuthenticationException("【access_token】不合法", RestCodeEnum.ERROR);
    }
    // 从 JWT 中获取用户名和所拥有的角色列表字符串
    String userName = claimMap.get(JwtManager.JWT_PUBLIC_CLAIM_USER_NAME_KEY).asString();
    String roles = claimMap.get(JwtManager.JWT_PUBLIC_CLAIM_ROLE_NAME_KEY).asString();

    // 3、查看缓存中是否存在该 access token
    CacheManager cacheManager = CacheManager.getInstance();
    LoadingCache<String, Object> cache = cacheManager.getCache30MinutesToLive();
    if (cache.getIfPresent(userName) == null)
      throw new AuthenticationException("【access_token】已过期", RestCodeEnum.ERROR);

    // 密码置空即可
    String password = "";

    // 将以英文逗号隔开的角色名转化为 Spring Security 需要的角色列表
    List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList(roles);

    return new UsernamePasswordAuthenticationToken(userName, password, authorityList);
  }
}