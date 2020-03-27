package net.wuxianjie.springbootvuejs.rest;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 包装最终控制器的响应结果，故控制器最终返回对象应该是 {@link Object} 或 {@link ResponseEntity}
 *
 * @author 吴仙杰
 */
@Aspect
@Component
@Slf4j
public class RestResponseEntityWrapperAspect {

  @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
  public void anyControllerPointcut() {

  }

  @Pointcut("execution(* *(..))")
  public void anyMethodPointcut() {

  }

  @Around("anyControllerPointcut() && anyMethodPointcut()")
  public Object wrapControllerResult(ProceedingJoinPoint joinPoint) throws Throwable {
    Object result = joinPoint.proceed();
    if (!(result instanceof ResponseEntity)) {
      return new ResponseEntity<>(result, HttpStatus.OK);
    }
    return result;
  }
}
