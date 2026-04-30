package jp.ne.yonem.restful.infrastructure.lesson.chain;

/** メッセージ送信を抽象化するインターフェースです。 */
interface MessageSender {
  void send(String message);
}
