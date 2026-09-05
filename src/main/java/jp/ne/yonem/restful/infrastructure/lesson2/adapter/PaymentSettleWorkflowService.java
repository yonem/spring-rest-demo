package jp.ne.yonem.restful.infrastructure.lesson2.adapter;

import java.util.Objects;
import org.springframework.stereotype.Service;

/** 決済処理ワークフローを実行するサービスです。 */
@Service
public class PaymentSettleWorkflowService {

  /**
   * 指定された決済プロセッサーを用いて決済を実行します。
   *
   * @param processor 決済プロセッサー
   * @param accountId アカウントID
   * @param amount 決済金額
   * @return 処理結果
   */
  public String execute(PaymentProcessor processor, String accountId, int amount) {
    var safeProcessor = Objects.requireNonNull(processor, "processor must not be null");
    var safeAccountId = Objects.requireNonNull(accountId, "accountId must not be null");

    return safeProcessor.processPayment(safeAccountId, amount);
  }
}
