package net.wuxianjie.springbootvuejs.rest;

import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.wuxianjie.springbootvuejs.exception.AuthenticationException;
import net.wuxianjie.springbootvuejs.exception.BaseException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 自定义全局异常处理
 *
 * @author 吴仙杰
 */
@Slf4j
@RestControllerAdvice
public class RestExceptionController {

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public RestResult<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
    return new RestResult<>(RestCodeEnum.ERROR, e.getMessage());
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public RestResult<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
    String message = String.format("缺少参数【%s】，详细信息：【%s】",e.getParameterName(), e.getMessage());
    log.warn(message);
    return new RestResult<>(RestCodeEnum.ERROR, message);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public RestResult<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    List<String> errorList = new ArrayList<>();
    BindingResult bindingResult = e.getBindingResult();
    List<FieldError> fieldErrors = bindingResult.getFieldErrors();
    for (FieldError error : fieldErrors) {
      String errorMsg = String.format("参数【%s】，值【%s】，校验不通过【%s】", error.getField(), error.getRejectedValue(), error.getDefaultMessage());
      errorList.add(errorMsg);
    }

    String message = Joiner.on("；").join(errorList);
    log.warn(message);
    return new RestResult<>(RestCodeEnum.ERROR, message);
  }

  @ExceptionHandler(AuthenticationException.class)
  public RestResult<Void> handleAuthenticationException(AuthenticationException e) {
    return new RestResult<>(RestCodeEnum.ERROR, e.getMessage());
  }

  @ExceptionHandler(BaseException.class)
  public RestResult<Void> handleBaseException(BaseException e) {
    log.error("自定义异常", e);
    return new RestResult<>(RestCodeEnum.ERROR, e.getMessage());
  }

  @ExceptionHandler(Throwable.class)
  public RestResult<Void> handleThrowable(Throwable t) {
    String message = String.format("异常消息【%s】，异常类【%s】", t.getMessage(), t.getClass().getName());
    log.error("默认异常", t);
    return new RestResult<>(RestCodeEnum.ERROR, message);
  }
}
