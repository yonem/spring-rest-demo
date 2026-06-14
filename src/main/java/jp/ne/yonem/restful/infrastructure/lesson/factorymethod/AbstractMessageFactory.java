package jp.ne.yonem.restful.infrastructure.lesson.factorymethod;

import java.util.Objects;

/** 製品を生成して処理を行う抽象工場クラスです。 */
public abstract class AbstractMessageFactory {

  /**
   * メッセージを生成して送信の準備を行います。 サブクラスによるアルゴリズムの破壊を防ぐため final とします。
   *
   * @return 送信メッセージ内容
   */
  public final String prepareMessage() {
    // 実際の生成はサブクラスの factoryMethod に委ねる
    var product = createProduct();
    var safeProduct = Objects.requireNonNull(product, "FactoryProduct must not be null");

    return safeProduct.getContent();
  }

  /**
   * 具体的な製品を生成するファクトリメソッドです。
   *
   * @return 生成された製品
   */
  protected abstract FactoryProduct createProduct();
}
