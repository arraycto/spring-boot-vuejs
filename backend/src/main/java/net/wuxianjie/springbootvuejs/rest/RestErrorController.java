package net.wuxianjie.springbootvuejs.rest;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

/**
 * 修改 Spring Boot 默认白板页
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
  public RestResult<Void> handleError(WebRequest request) {
    Map<String, Object> errorMap = getErrorAttributes(request);
    String path = (String) errorMap.get("path");
    String errorMessage = (String) errorMap.get("message");
    String errorType = (String) errorMap.get("error");
    int httpStatus = (int) errorMap.get("status");
    String message = String.format("请求【%s】时出错，错误消息【%s】（类型【%s】，HTTP 状态码【%d】)", path, errorMessage, errorType, httpStatus);
    return new RestResult<>(RestCodeEnum.ERROR, message);
  }

  @Override
  public String getErrorPath() {
    return "/error";
  }

  private Map<String, Object> getErrorAttributes(WebRequest request) {
    return new HashMap<>(errorAttributes.getErrorAttributes(request, false));
  }
}
