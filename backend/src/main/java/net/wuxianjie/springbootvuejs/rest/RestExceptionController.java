package net.wuxianjie.springbootvuejs.rest;

import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import net.wuxianjie.springbootvuejs.constants.RestCodeEnum;
import net.wuxianjie.springbootvuejs.exception.BaseException;
import net.wuxianjie.springbootvuejs.exception.JwtAuthenticationException;
import net.wuxianjie.springbootvuejs.exception.RequestArgumentNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
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
   * 处理因客户端请求方法与服务端可接受方法不一致时的异常
   *
   * @param e 自动注入的异常
   * @return 通用结果封装
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<RestResultDto<Void>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
    return RestApiUtils.generateError(RestCodeEnum.HTTP_REQUEST_METHOD_NOT_SUPPORTED, e.getMessage());
  }

  /**
   * 处理因客户端请求头 MIME 与服务端可接受 MIME 不一致时的异常
   *
   * @param e 自动注入的异常
   * @return 通用结果封装
   */
  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<RestResultDto<Void>> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
    return RestApiUtils.generateError(RestCodeEnum.HTTP_REQUEST_NOT_ACCEPTABLE, e.getMessage());
  }

  /**
   * 处理因客户端没有指定服务端必要请求体时的异常
   *
   * @return 通用结果封装
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<RestResultDto<Void>> handleHttpMessageNotReadableException() {
    return RestApiUtils.generateError(RestCodeEnum.MISSING_REQUIRED_PARAMETER, "缺少所需的请求正文");
  }

  /**
   * 处理因客户端没有传入服务端必要参数（由 {@code @RequestParam} 指定）时的异常
   *
   * @param e 自动注入的异常
   * @return 通用结果封装
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<RestResultDto<Void>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
    String message = String.format("缺少参数【%s】，详细信息：%s",e.getParameterName(), e.getMessage());
    log.warn(message);
    return RestApiUtils.generateError(RestCodeEnum.MISSING_REQUIRED_PARAMETER, message);
  }

  /**
   * 处理因客户端传参不符合服务端参数要求时的异常（{@code @valid}）
   *
   * @param e 自动注入的异常
   * @return 通用结果封装
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<RestResultDto<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    List<String> errorList = new ArrayList<>();
    BindingResult bindingResult = e.getBindingResult();
    List<FieldError> fieldErrors = bindingResult.getFieldErrors();
    for (FieldError error : fieldErrors) {
      String errorMsg = String.format("参数【%s】，值【%s】，校验不通过：%s", error.getField(), error.getRejectedValue(), error.getDefaultMessage());
      errorList.add(errorMsg);
    }

    String message = Joiner.on("；").join(errorList);
    log.warn(message);
    return RestApiUtils.generateError(RestCodeEnum.MALFORMED_PARAMETER, message);
  }

  /**
   处理因客户端传参不符合服务端参数要求时的异常（（{@code @Validated}））
   *
   * @param e 自动注入的异常
   * @return 通用结果封装
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<RestResultDto<Void>> handleConstraintViolationException(ConstraintViolationException e) {
    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
    Set<String> messages = new HashSet<>(violations.size());
    messages.addAll(violations.stream()
      .map(violation -> String.format("参数【%s】，值【%s】，校验不通过：%s",
        violation.getPropertyPath(), violation.getInvalidValue(), violation.getMessage()))
      .collect(Collectors.toSet()));
    String message = Joiner.on(";").join(messages);
    log.warn(message);
    return RestApiUtils.generateError(RestCodeEnum.MALFORMED_PARAMETER, message);
  }

  /**
   * 处理因客户端参数校验失败时的异常
   *
   * @param e 自动注入的异常
   * @return 通用结果封装
   */
  @ExceptionHandler(RequestArgumentNotValidException.class)
  public ResponseEntity<RestResultDto<Void>> handleRequestArgumentNotValidException(RequestArgumentNotValidException e) {
    log.warn(e.getMessage());
    return RestApiUtils.generateError(e.getCode(), e.getMessage());
  }

  /**
   * 处理因客户端鉴权失败时的异常
   *
   * @param e 自动注入的异常
   * @return 通用结果封装
   */
  @ExceptionHandler(JwtAuthenticationException.class)
  public ResponseEntity<RestResultDto<Void>> handleAuthenticationException(JwtAuthenticationException e) {
    return RestApiUtils.generateError(e.getCode(), e.getMessage());
  }

  /**
   * 处理所有继承了自定义基类的异常
   *
   * @param e 自动注入的异常
   * @return 通用结果封装
   */
  @ExceptionHandler(BaseException.class)
  public ResponseEntity<RestResultDto<Void>> handleBaseException(BaseException e) {
    log.error("自定义异常", e);
    return RestApiUtils.generateError(e.getCode(), e.getMessage());
  }

  /**
   * 处理所有异常
   *
   * @param t 自动注入的异常
   * @return 通用结果封装
   */
  @ExceptionHandler(Throwable.class)
  public ResponseEntity<RestResultDto<Void>> handleThrowable(Throwable t) {
    String message = String.format("异常消息【%s】，异常类【%s】", t.getMessage(), t.getClass().getName());
    log.error("默认异常", t);
    return RestApiUtils.generateError(RestCodeEnum.ERROR_SERVER, message);
  }
}
