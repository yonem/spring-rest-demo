package jp.ne.yonem.restful.infrastructure.lesson.chain;

/** 形式に応じた加工（Strategy）担当。 */
class FormattingHandler extends MessageHandler {
  @Override
  public String handle(String message) {
    var strategy =
        switch (message) {
          case String s when s.startsWith("[U]") -> MessageStrategy.UPPER;
          case String s when s.startsWith(" ") -> MessageStrategy.TRIM;
          default -> MessageStrategy.DEFAULT;
        };
    return invokeNext(strategy.apply(message));
  }
}
