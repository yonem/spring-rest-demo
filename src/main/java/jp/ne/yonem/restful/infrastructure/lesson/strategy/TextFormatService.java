package jp.ne.yonem.restful.infrastructure.lesson.strategy;

import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

/** 文字列の加工処理を管理するサービスです。 */
@Service
public class TextFormatService {

  /**
   * 指定された複数の戦略を順番に適用して文字列を加工します。
   *
   * @param text 対象文字列
   * @param strategies 適用する戦略のリスト
   * @return 加工後の文字列
   */
  public String execute(String text, List<TextProcessStrategy> strategies) {
    if (Objects.isNull(text)) return null;
    var safeStrategies = Objects.requireNonNullElse(strategies, List.<TextProcessStrategy>of());
    var result = text;

    for (var strategy : safeStrategies) {
      result = strategy.apply(result);
    }
    return result;
  }
}
