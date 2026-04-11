package jp.ne.yonem.restful.infrastructure.lesson.strategy;

import org.springframework.stereotype.Service;

/** 自動判定による文字列加工サービスです。 */
@Service
public class TextAutoFormatService {

  /**
   * 文字列の内容から自動的に最適な加工を適用します。
   *
   * @param text 対象文字列
   * @return 加工後の文字列
   */
  public String execute(String text) {

    // Factoryに戦略の選定を任せる
    var strategy = TextProcessFactory.create(text);

    // 選ばれた戦略を実行するだけ
    return strategy.apply(text);
  }
}
