package jp.ne.yonem.restful.infrastructure.lesson.chain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** メッセージの自動検閲を有効にするアノテーションです。 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Censorship {}
