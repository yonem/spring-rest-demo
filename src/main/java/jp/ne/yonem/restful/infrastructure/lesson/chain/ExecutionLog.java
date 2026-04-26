package jp.ne.yonem.restful.infrastructure.lesson.chain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** メソッドの実行ログと処理時間を記録するアノテーションです。 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExecutionLog {}
