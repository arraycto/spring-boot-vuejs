package net.wuxianjie.springbootvuejs.rest;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.wuxianjie.springbootvuejs.exception.AuthenticationException;
import net.wuxianjie.springbootvuejs.util.JsonUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 定义 filter 异常处理过滤器
 *
 * <p>原因：Spring 全局异常处理不能处理 filter 中的异常，比如 Spring Security 中抛出的异常</p>
 *
 * @author 吴仙杰
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class ExceptionHandlerFilter implements Filter {

  private static final String HTTP_HEADER_ACCEPT = "Accept";

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    HttpServletResponse httpServletResponse = (HttpServletResponse) response;

    try {
      chain.doFilter(request, response);
    } catch (RuntimeException e) {
      RestResult<Void> result = getRestResult(httpServletRequest, httpServletResponse, e);
      handleResponse(httpServletResponse, result, e);
    }
  }

  @Override
  public void destroy() {

  }

  private RestResult<Void> getRestResult(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, RuntimeException e) {
    // 记录过滤器链中出现的运行时异常日志
    if (!(e instanceof AuthenticationException)) {
      // 记录非鉴权失败的日志
      log.error(String.format("请求【%s】时出现异常：%s", httpServletRequest.toString(), e.getMessage()), e);
    }

    return new RestResult<>(RestCodeEnum.ERROR, e.getMessage());
  }

  private void handleResponse(HttpServletResponse httpServletResponse, RestResult<Void> result,
      RuntimeException e)
      throws IOException {
    httpServletResponse.setHeader("Content-Type", "application/json; charset=utf8");
    httpServletResponse.getWriter().write(JsonUtils.toJson(result, true));
  }
}
