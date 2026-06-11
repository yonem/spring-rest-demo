package jp.ne.yonem.restful.infrastructure.lesson.factorymethod;

/** 具体的な製品（Slack用）です。 */
final class SlackFactoryProduct implements FactoryProduct {

  @Override
  public String getContent() {
    return "[Slack] 通知を配信します。";
  }
}
