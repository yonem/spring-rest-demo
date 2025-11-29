package jp.ne.yonem.restful.demo.config.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {

  @Around("execution(* jp.ne.yonem.restful.demo.service.*.*(..))")
  public Object measureTime(ProceedingJoinPoint joinPoint) throws Throwable {
    var startTime = System.currentTimeMillis();
    var result = joinPoint.proceed();
    var endTime = System.currentTimeMillis();
    var executionTime = endTime - startTime;
    var className = joinPoint.getTarget().getClass().getSimpleName();
    var methodName = joinPoint.getSignature().getName();
    log.info("⏳ {}.{} の実行時間: {} ms", className, methodName, executionTime);
    return result;
  }
}
