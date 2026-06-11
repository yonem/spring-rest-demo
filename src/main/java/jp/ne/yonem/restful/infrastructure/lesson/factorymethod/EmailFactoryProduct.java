package jp.ne.yonem.restful.infrastructure.lesson.factorymethod;

/** 具体的な製品（Email用）です。 */
final class EmailFactoryProduct implements FactoryProduct {

  @Override
  public String getContent() {
    return "[Email] 通知を配信します。";
  }
}
