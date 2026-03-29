package jp.ne.yonem.restful.infrastructure.lesson.observer;

import jp.ne.yonem.restful.infrastructure.lesson.observer.OrderSettleService.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/** 注文完了イベントを購読し、メール送信処理を行うリスナーです。 */
@Component
@Slf4j
public class MailListener {

  /**
   * 注文完了イベントを検知してメール送信のログを出力します。
   *
   * @param event 注文完了イベント
   */
  @EventListener
  public void onOrderComplete(OrderEvent event) {
    log.info("メール送信を実行しました。 顧客ID: {}, 注文ID: {}", event.customerId(), event.orderId());
  }
}
