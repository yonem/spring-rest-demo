package jp.ne.yonem.restful.infrastructure.lesson2.facade;

import org.springframework.stereotype.Component;

/** サブシステム2: 請求・決済サブシステム */
@Component
public class BillingService {

  public boolean hasOutstandingBalance(String userId) {
    // 未払い金の有無チェック
    return false;
  }
}
