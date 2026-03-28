package jp.ne.yonem.restful.infrastructure.lesson.observer;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 注文の精算処理を行うサービスです。 */
@Service
@RequiredArgsConstructor
public class OrderSettleService {

  private final ApplicationEventPublisher publisher;

  /** 注文完了イベントの情報を保持するレコードです。 */
  public record OrderEvent(
      String orderId, long customerId, int totalAmount, LocalDateTime orderedAt) {}

  /**
   * 注文を確定させ、登録されたリスナーへ通知を飛ばします。
   *
   * @param orderId 注文ID
   * @param customerId 顧客ID
   * @param amount 合計金額
   */
  @Transactional
  public void execute(String orderId, long customerId, int amount) {
    var event = new OrderEvent(orderId, customerId, amount, LocalDateTime.now());
    publisher.publishEvent(event);
  }
}
