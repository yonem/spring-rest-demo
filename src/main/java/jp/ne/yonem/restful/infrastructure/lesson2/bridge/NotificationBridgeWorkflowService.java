package jp.ne.yonem.restful.infrastructure.lesson2.bridge;

import java.util.Objects;
import org.springframework.stereotype.Service;

/** Bridgeパターンを利用してメッセージ通知を送信するサービスです。 */
@Service
public class NotificationBridgeWorkflowService {

  /**
   * 指定された通知機能と実装の組み合わせでメッセージを送信します。
   *
   * @param notification 通知機能オブジェクト（Abstraction）
   * @param title タイトル
   * @param body 本文
   * @return 送信結果文字列
   */
  public String execute(NotificationAbstraction notification, String title, String body) {
    var safeNotification = Objects.requireNonNull(notification, "notification must not be null");
    var safeTitle = Objects.requireNonNull(title, "title must not be null");
    var safeBody = Objects.requireNonNull(body, "body must not be null");

    return safeNotification.notify(safeTitle, safeBody);
  }
}
