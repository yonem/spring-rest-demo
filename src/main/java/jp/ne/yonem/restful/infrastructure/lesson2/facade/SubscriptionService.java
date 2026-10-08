package jp.ne.yonem.restful.infrastructure.lesson2.facade;

import org.springframework.stereotype.Component;

/** サブシステム1: 定期課金サブシステム */
@Component
public class SubscriptionService {

  public boolean cancelSubscription(String userId) {
    // サブシステムの個別処理
    return true;
  }
}
