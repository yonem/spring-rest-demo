package jp.ne.yonem.restful.infrastructure.lesson.chain;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/** 実行ログとパフォーマンス計測を制御するアスペクトです。 */
@Aspect
@Component
public class ExecutionLogAspect {

  /**
   * {@link ExecutionLog} が付与されたメソッドの実行時間を計測しログ出力します。
   *
   * @param joinPoint 実行ポイント
   * @return メソッドの実行結果
   * @throws Throwable 実行エラー
   */
  @Around("@annotation(jp.ne.yonem.restful.infrastructure.lesson.chain.ExecutionLog)")
  public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {
    var start = System.currentTimeMillis();
    var methodName = joinPoint.getSignature().getName();

    try {
      return joinPoint.proceed();
    } finally {
      var executionTime = System.currentTimeMillis() - start;
      System.out.println("Method [%s] executed in %d ms".formatted(methodName, executionTime));
    }
  }
}
