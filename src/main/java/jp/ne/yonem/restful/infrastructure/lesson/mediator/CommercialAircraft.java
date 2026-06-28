package jp.ne.yonem.restful.infrastructure.lesson.mediator;

import java.util.Objects;

/** 旅客機を表す具体的な飛行機クラスです。 */
public class CommercialAircraft extends AircraftColleague {

  public CommercialAircraft(ControlTowerMediator mediator, String callSign) {
    super(mediator, callSign);
  }

  /**
   * メッセージを管制塔経由で送信します。
   *
   * @param message 送信内容
   */
  public void send(String message) {
    var safeMessage = Objects.requireNonNull(message, "message must not be null");
    this.mediator.sendMessage(safeMessage, this);
  }

  @Override
  public void receive(String message) {
    var safeMessage = Objects.requireNonNull(message, "message must not be null");
    System.out.println("[%s] 受信メッセージ: %s".formatted(this.callSign, safeMessage));
  }
}
