package net.wuxianjie.springbootvuejs.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

/**
 * 在每次请求后清除 Spring Security {@link SecurityContextHolder}
 *
 * <p>无状态的 RESTFul Web 服务不要使用 HTTP 会话，并会在每次请求时重新进行身份验证</p>
 *
 * @author 吴仙杰
 */
public class SecurityContextFilter extends SecurityContextPersistenceFilter {

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
    chain.doFilter(req, res);
  }
}
