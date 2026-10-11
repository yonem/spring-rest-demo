package jp.ne.yonem.restful.infrastructure.lesson2.facade;

import java.util.Objects;
import org.springframework.stereotype.Component;

/** 退会手続きを一元管理するファサードクラスです。 */
@Component
public class UserDeactivationFacade {

  private final SubscriptionService subscriptionService;
  private final BillingService billingService;
  private final UserAccountService userAccountService;

  public UserDeactivationFacade(
      SubscriptionService subscriptionService,
      BillingService billingService,
      UserAccountService userAccountService) {
    this.subscriptionService =
        Objects.requireNonNull(subscriptionService, "subscriptionService must not be null");
    this.billingService = Objects.requireNonNull(billingService, "billingService must not be null");
    this.userAccountService =
        Objects.requireNonNull(userAccountService, "userAccountService must not be null");
  }

  /**
   * 退会に必要な一連のサブシステム処理を順序通り実行します。
   *
   * @param userId ユーザーID
   * @return 退会処理結果メッセージ
   */
  public String processDeactivation(String userId) {
    var safeUserId = Objects.requireNonNull(userId, "userId must not be null");

    // 1. 未払い金チェック
    if (billingService.hasOutstandingBalance(safeUserId)) {
      return "FAILED: Outstanding balance exists";
    }

    // 2. 定期購入解約
    subscriptionService.cancelSubscription(safeUserId);

    // 3. アカウント無効化
    userAccountService.deactivateAccount(safeUserId);

    return "SUCCESS: User[%s] deactivated".formatted(safeUserId);
  }
}
