package jp.ne.yonem.restful.infrastructure.lesson2.adapter;

import java.util.Objects;

/** レガシー決済システムを新決済インターフェースへ適合させるアダプターです。 */
public record LegacyPaymentAdapter(LegacyPaymentGateway legacyGateway) implements PaymentProcessor {

  public LegacyPaymentAdapter(LegacyPaymentGateway legacyGateway) {
    this.legacyGateway = Objects.requireNonNull(legacyGateway, "legacyGateway must not be null");
  }

  @Override
  public String processPayment(String accountId, int amountYen) {
    var safeAccountId = Objects.requireNonNull(accountId, "accountId must not be null");

    if (amountYen <= 0) {
      throw new IllegalArgumentException("amountYen must be greater than 0");
    }

    // 引数の順序や戻り値の形式をAdapteeに合わせて変換
    int resultCode = legacyGateway.executeTransaction(amountYen, safeAccountId);

    if (resultCode == 200) {
      return "SUCCESS: Account[%s] Amount[%d]".formatted(safeAccountId, amountYen);
    }
    return "FAILURE: Account[%s]".formatted(safeAccountId);
  }
}
