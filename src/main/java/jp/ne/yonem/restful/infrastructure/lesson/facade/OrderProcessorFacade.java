package jp.ne.yonem.restful.infrastructure.lesson.facade;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** 注文に関する複雑なサブシステムをまとめる窓口（Facade）クラスです。 */
@Component
@RequiredArgsConstructor
public class OrderProcessorFacade {

  private final InventoryService inventoryService;
  private final PaymentService paymentService;
  private final ShippingService shippingService;

  /**
   * 一連の注文フローを実行します。
   *
   * @param productId 商品ID
   * @param price 価格
   * @return 注文成功時はtrue
   */
  public boolean processOrder(String productId, int price) {
    Objects.requireNonNull(productId, "productId must not be null");

    // 1. 在庫確認
    if (!inventoryService.checkStock(productId)) {
      return false;
    }

    // 2. 決済
    if (!paymentService.process(price)) {
      return false;
    }

    // 3. 配送予約
    shippingService.reserve(productId);

    return true;
  }
}
