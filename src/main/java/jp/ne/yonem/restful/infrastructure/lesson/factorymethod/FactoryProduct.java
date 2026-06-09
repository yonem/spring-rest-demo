package jp.ne.yonem.restful.infrastructure.lesson.factorymethod;

/** 送信対象となるメッセージを表す製品インターフェースです。 */
public interface FactoryProduct {
  String getContent();
}
