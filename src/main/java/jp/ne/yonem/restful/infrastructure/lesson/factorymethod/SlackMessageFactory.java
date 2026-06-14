package jp.ne.yonem.restful.infrastructure.lesson.factorymethod;

/** Slack用のメッセージを生成する具体的な工場です。 */
public class SlackMessageFactory extends AbstractMessageFactory {

  @Override
  protected FactoryProduct createProduct() {
    return new SlackFactoryProduct();
  }
}
