package net.wuxianjie.springbootvuejs.rest;

import java.util.HashMap;
import java.util.Map;
import net.wuxianjie.springbootvuejs.constants.RestCodeEnum;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

/**
 * 覆盖 Spring Boot 默认白板页
 *
 * @author 吴仙杰
 */
@RestController
public class RestErrorController implements ErrorController {

  private final ErrorAttributes errorAttributes;

  public RestErrorController(ErrorAttributes errorAttributes) {
    this.errorAttributes = errorAttributes;
  }

  @RequestMapping("/error")
  public ResponseEntity<RestResultDto<Void>> handleError(WebRequest request) {
    Map<String, Object> errorMap = getErrorAttributes(request);
    String path = (String) errorMap.get("path");
    String errorMessage = (String) errorMap.get("message");
    String errorType = (String) errorMap.get("error");
    int httpStatus = (int) errorMap.get("status");

    // 2、以 WAR 部署时，Spring Boot 会自动处理掉 Spring Security 过滤器 `JwtAuthenticationFilter` 中所抛出的自定义认证异常并以 `error` 级别打印日志，且不会再在 `ExceptionHandlerFilter` 中得到异常，故通过字符串匹配以返回正确的认证结果及错误码
    if (errorMessage.contains("【access_token】缺失"))
      return RestApiUtils.generateError(RestCodeEnum.MISSING_ACCESS_TOKEN, errorMessage);
    if (errorMessage.contains("【access_token】已失效"))
      return RestApiUtils.generateError(RestCodeEnum.INVALID_ACCESS_TOKEN, errorMessage);
    if (errorMessage.contains("【access_token】已过期"))
      return RestApiUtils.generateError(RestCodeEnum.EXPIRED_ACCESS_TOKEN, errorMessage);
    if (errorMessage.contains("【access_token】已更新"))
      return RestApiUtils.generateError(RestCodeEnum.EXPIRED_ACCESS_TOKEN, errorMessage);

    // 404 处理
    if (httpStatus == HttpStatus.NOT_FOUND.value())
      return RestApiUtils.generateError(RestCodeEnum.Not_Found, "未找到与该请求地址所匹配的服务");

    String message = String.format("请求路径【%s】，类型【%s】，错误消息：%s", path, errorType, errorMessage);
    RestCodeEnum codeEnum = RestCodeEnum.resolve(httpStatus);
    return RestApiUtils.generateError(codeEnum == null ? RestCodeEnum.NO_MAPPING_HTTP_STATUS : codeEnum, message);
  }

  @Override
  public String getErrorPath() {
    return "/error";
  }

  private Map<String, Object> getErrorAttributes(WebRequest request) {
    return new HashMap<>(errorAttributes.getErrorAttributes(request, false));
  }
}
