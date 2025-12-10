package jp.ne.yonem.restful.config.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {

  @Around(
      "execution(* jp.ne.yonem.restful..*Service.*(..)) || "
          + "execution(* jp.ne.yonem.restful..*Provider.*(..))")
  public Object measureTime(ProceedingJoinPoint joinPoint) throws Throwable {
    var startTime = System.currentTimeMillis();
    var result = joinPoint.proceed(); // 実際のメソッドが実行される
    var endTime = System.currentTimeMillis();
    var executionTime = endTime - startTime;
    var className = joinPoint.getTarget().getClass().getSimpleName();
    var methodName = joinPoint.getSignature().getName();
    log.info("⏳ {}.{} の実行時間: {} ms", className, methodName, executionTime);
    return result;
  }

  @Before("execution(* jp.ne.yonem.restful..*Controller.*(..))")
  public void logRequestStart(JoinPoint joinPoint) {
    var className = joinPoint.getTarget().getClass().getSimpleName();
    var methodName = joinPoint.getSignature().getName();
    var args = joinPoint.getArgs();
    log.info("▶️ Request Start: {}.{} called with arguments: {}", className, methodName, args);
  }
}
