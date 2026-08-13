package jp.ne.yonem.restful.infrastructure.lesson2.abstractfactory;

import java.util.Objects;
import org.springframework.stereotype.Service;

/** Abstract Factoryを利用して通知コンポーネントの構築と配信を統括するサービスです。 */
@Service
public class PlatformNotificationWorkflowService {

  /**
   * 指定された工場から適切な製品群を取得し、通知ワークフローを実行します。
   *
   * @param factory コンポーネント生成工場
   * @param message 配信メッセージ
   * @return 実行結果ログ
   */
  public String execute(NotificationComponentFactory factory, String message) {
    var safeFactory = Objects.requireNonNull(factory, "factory must not be null");
    var safeMessage = Objects.requireNonNull(message, "message must not be null");

    // 一貫したテーマ・プラットフォームの製品セットをまとめて生成
    var card = safeFactory.createCard();
    var sender = safeFactory.createSender();

    return card.render() + " -> " + sender.send(safeMessage);
  }
}
