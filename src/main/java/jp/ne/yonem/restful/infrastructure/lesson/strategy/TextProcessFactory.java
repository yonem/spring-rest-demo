package jp.ne.yonem.restful.infrastructure.lesson.strategy;

import java.util.Objects;

/** 文字列の内容に基づいて最適な加工戦略を生成するファクトリです。 */
public class TextProcessFactory {

  /**
   * 入力文字列の内容を判定し、適切な戦略を返します。
   *
   * @param text 加工対象の文字列
   * @return 選択された戦略
   */
  public static TextProcessStrategy create(String text) {

    if (Objects.isNull(text)) {
      return TextProcessStrategy.NONE;
    }

    // 特定の条件（例：[U]で始まるなら大文字化）に基づいて戦略を切り替える
    if (text.startsWith("[U]")) {
      return TextProcessStrategy.UPPER;
    }

    if (text.startsWith(" ")) {
      return TextProcessStrategy.TRIM;
    }
    return TextProcessStrategy.NONE;
  }
}
