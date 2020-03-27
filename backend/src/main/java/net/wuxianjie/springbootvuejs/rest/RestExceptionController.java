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

  /**
   * 处理因客户端请求方法与服务端可接受方法不匹配时的异常
   *
   * @param e 自动注入的异常
   * @return 通用结果封装
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public RestResult<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
    return new RestResult<>(RestCodeEnum.ERROR, e.getMessage());
  }

  /**
   * 处理因客户端没有传入服务端必要参数（由 {@code @RequestParam} 指定）时的异常
   *
   * @param e 自动注入的异常
   * @return 通用结果封装
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public RestResult<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
    String message = String.format("缺少参数【%s】，详细信息：【%s】",e.getParameterName(), e.getMessage());
    log.warn(message);
    return new RestResult<>(RestCodeEnum.ERROR, message);
  }

  /**
   * 处理因客户端传参不符合服务端参数要求时的异常
   *
   * @param e 自动注入的异常
   * @return 通用结果封装
   */
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

  /**
   * 处理因客户端鉴权失败时的异常
   *
   * @param e 自动注入的异常
   * @return 通用结果封装
   */
  @ExceptionHandler(AuthenticationException.class)
  public RestResult<Void> handleAuthenticationException(AuthenticationException e) {
    return new RestResult<>(RestCodeEnum.ERROR, e.getMessage());
  }

  /**
   * 处理所有继承了自定义基类的异常
   *
   * @param e 自动注入的异常
   * @return 通用结果封装
   */
  @ExceptionHandler(BaseException.class)
  public RestResult<Void> handleBaseException(BaseException e) {
    log.error("自定义异常", e);
    return new RestResult<>(RestCodeEnum.ERROR, e.getMessage());
  }

  /**
   * 处理所有异常
   *
   * @param t 自动注入的异常
   * @return 通用结果封装
   */
  @ExceptionHandler(Throwable.class)
  public RestResult<Void> handleThrowable(Throwable t) {
    String message = String.format("异常消息【%s】，异常类【%s】", t.getMessage(), t.getClass().getName());
    log.error("默认异常", t);
    return new RestResult<>(RestCodeEnum.ERROR, message);
  }
}