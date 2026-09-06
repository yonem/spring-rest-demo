package jp.ne.yonem.restful.infrastructure.lesson2.bridge;

/** 送信プラットフォームの実装を表すインターフェース（Implementor）です。 */
public interface MessageSenderImplementor {
  String sendRawMessage(String title, String body);
}
