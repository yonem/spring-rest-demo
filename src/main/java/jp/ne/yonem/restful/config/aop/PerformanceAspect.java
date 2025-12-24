package jp.ne.yonem.restful.config.aop;

import static org.springframework.http.HttpHeaders.USER_AGENT;
import static org.springframework.security.config.Elements.ANONYMOUS;
import static reactor.netty.Metrics.UNKNOWN;

import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {

  /** Service/Providerの実行時間計測 */
  @Around(
      "execution(* jp.ne.yonem.restful..*Service.*(..)) || "
          + "execution(* jp.ne.yonem.restful..*Provider.*(..))")
  public Object measureTime(ProceedingJoinPoint joinPoint) throws Throwable {
    var startTime = System.currentTimeMillis();

    try {
      return joinPoint.proceed();

    } finally {
      var executionTime = System.currentTimeMillis() - startTime;
      var className = joinPoint.getTarget().getClass().getSimpleName();
      var methodName = joinPoint.getSignature().getName();
      log.info("⏳ {}.{} の実行時間: {} ms", className, methodName, executionTime);
    }
  }

  /** Controllerの実行管理とMDCクリア */
  @Around("execution(* jp.ne.yonem.restful..*Controller.*(..))")
  public Object manageControllerContext(ProceedingJoinPoint joinPoint) throws Throwable {

    try {
      return joinPoint.proceed();

    } finally {
      MDC.clear();
    }
  }

  /** ユーザー情報・アクセス元・引数を統合して出力 */
  @Before("execution(* jp.ne.yonem.restful..*Controller.*(..))")
  public void logRequestStart(JoinPoint joinPoint) {
    var attributes = RequestContextHolder.getRequestAttributes();

    if (Objects.nonNull(attributes) && attributes instanceof ServletRequestAttributes) {
      var request = ((ServletRequestAttributes) attributes).getRequest();
      setupMdc(request);
    }
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    var username = Optional.ofNullable(authentication).map(Principal::getName).orElse(ANONYMOUS);
    var className = joinPoint.getTarget().getClass().getSimpleName();
    var methodName = joinPoint.getSignature().getName();
    var args = Arrays.toString(joinPoint.getArgs());

    log.info("▶️ Request Start: [USER:{}] {}.{} | Args: {}", username, className, methodName, args);
  }

  private void setupMdc(HttpServletRequest request) {
    var ip = getClientIp(Objects.requireNonNull(request));
    var url = request.getRequestURL().toString();
    var method = request.getMethod();
    var ua = Objects.requireNonNullElse(request.getHeader(USER_AGENT), UNKNOWN);

    MDC.put("client_ip", ip);
    MDC.put("request_url", url);
    MDC.put("http_method", method);
    MDC.put("user_agent", ua);
  }

  private String getClientIp(HttpServletRequest request) {
    var xff = request.getHeader("X-Forwarded-For");

    if (Objects.nonNull(xff) && !xff.isBlank()) {
      return xff.split(",")[0];
    }
    return Objects.requireNonNullElse(request.getRemoteAddr(), "0.0.0.0");
  }
}
