package jp.ne.yonem.restful.infrastructure.lesson.factorymethod;

/** Email用のメッセージを生成する具体的な工場です。 */
public class EmailMessageFactory extends AbstractMessageFactory {

  @Override
  protected FactoryProduct createProduct() {
    return new EmailFactoryProduct();
  }
}
