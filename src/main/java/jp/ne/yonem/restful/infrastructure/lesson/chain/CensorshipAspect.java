package jp.ne.yonem.restful.infrastructure.lesson.chain;

import java.util.Objects;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/** メッセージ検閲を横断的に適用するアスペクトです。 */
@Aspect
@Component
public class CensorshipAspect {

  /**
   * {@link Censorship} が付与されたメソッドに対し、入力値の検閲を実施します。
   *
   * @param joinPoint 実行ポイント
   * @return メソッドの実行結果（検閲NGの場合は[REJECTED]）
   * @throws Throwable 実行エラー
   */
  @Around("@annotation(jp.ne.yonem.restful.infrastructure.lesson.chain.Censorship)")
  public Object intercept(ProceedingJoinPoint joinPoint) throws Throwable {
    var args = joinPoint.getArgs();

    // 第一引数が文字列の場合のみ検閲を実施
    if (0 < args.length && args[0] instanceof String message) {
      var safeMessage = Objects.requireNonNullElse(message, "");
      if (safeMessage.contains("NG_WORD")) {
        return "[REJECTED]";
      }
    }

    return joinPoint.proceed();
  }
}
