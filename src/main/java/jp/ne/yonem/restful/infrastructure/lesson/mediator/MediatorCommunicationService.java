package jp.ne.yonem.restful.infrastructure.lesson.mediator;

import java.util.Objects;
import org.springframework.stereotype.Service;

/** メディエーターパターンによる通信をシミュレートするサービスです。 */
@Service
public class MediatorCommunicationService {

  /**
   * 飛行機から管制塔へメッセージを送信させ、全体の連携を行います。
   *
   * @param aircraft 発信元の飛行機
   * @param message 配信メッセージ
   */
  public void execute(CommercialAircraft aircraft, String message) {
    var safeAircraft = Objects.requireNonNull(aircraft, "aircraft must not be null");
    var safeMessage = Objects.requireNonNull(message, "message must not be null");

    // 飛行機は直接他の飛行機を呼ぶのではなく、自身の保持するmediatorへ通信を委ねる
    safeAircraft.send(safeMessage);
  }
}
