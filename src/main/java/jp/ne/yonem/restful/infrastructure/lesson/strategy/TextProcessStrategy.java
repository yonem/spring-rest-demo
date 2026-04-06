package jp.ne.yonem.restful.infrastructure.lesson.strategy;

import java.util.Objects;
import java.util.function.UnaryOperator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** 文字列の加工戦略を定義する列挙型です。 */
@Getter
@RequiredArgsConstructor
public enum TextProcessStrategy {

  /** すべて大文字に変換します。 */
  UPPER(String::toUpperCase),

  /** 前後の空白を除去します。 */
  TRIM(String::trim),

  /** 何もしません。 */
  NONE(s -> s);

  private final UnaryOperator<String> operator;

  /**
   * 戦略に基づいて文字列を加工します。
   *
   * @param text 対象文字列
   * @return 加工後文字列
   */
  public String apply(String text) {
    if (Objects.isNull(text)) return null;
    return operator.apply(text);
  }
}
