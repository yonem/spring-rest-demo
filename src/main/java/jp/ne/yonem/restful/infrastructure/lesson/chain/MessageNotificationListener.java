package jp.ne.yonem.restful.infrastructure.lesson.chain;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/** 加工完了イベントを購読し、結果を表示するリスナーです。 */
@Component
public class MessageNotificationListener {

  /**
   * 加工完了イベントをコンソールに出力します。
   *
   * @param event 加工完了イベント
   */
  @EventListener
  public void onMessageProcessed(MessageProcessedEvent event) {
    var content = event.content();
    System.out.println("通知を受信しました: " + content);
  }
}
