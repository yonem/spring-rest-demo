package jp.ne.yonem.restful.infrastructure.lesson.facade;

import org.springframework.stereotype.Component;

/** 在庫管理サブシステムです。 */
@Component
class InventoryService {
  public boolean checkStock(String productId) {
    return true;
  }
}
