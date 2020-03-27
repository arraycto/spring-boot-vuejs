package net.wuxianjie.springbootvuejs.rest;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.wuxianjie.springbootvuejs.constants.RestCodeEnum;
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

  @Override
  public void init(FilterConfig filterConfig) {

  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    HttpServletResponse httpServletResponse = (HttpServletResponse) response;

    try {
      chain.doFilter(request, response);
    } catch (Exception e) {
      RestResultDto<Void> result = getRestResult(httpServletRequest, e);
      handleResponse(httpServletResponse, result);
    }
  }

  @Override
  public void destroy() {

  }

  private RestResultDto<Void> getRestResult(HttpServletRequest httpServletRequest, Exception e) {
    // 记录过滤器链中出现的运行时异常日志
    if (e instanceof AuthenticationException) {
      AuthenticationException authError = (AuthenticationException) e;
      return new RestResultDto<>(authError.getCode(), authError.getMessage(), null);
    }

    // 记录非鉴权失败的日志
    log.error(String.format("请求【%s】时出现异常：%s", httpServletRequest.toString(), e.getMessage()), e);
    return new RestResultDto<>(RestCodeEnum.ERROR_SERVER, e.getMessage(), null);
  }

  private void handleResponse(HttpServletResponse httpServletResponse, RestResultDto<Void> result) throws IOException {
    httpServletResponse.setHeader("Content-Type", "application/json; charset=utf8");
    httpServletResponse.setStatus(result.getHttpStatus());
    httpServletResponse.getWriter().write(JsonUtils.toJson(result, true));
  }
}
