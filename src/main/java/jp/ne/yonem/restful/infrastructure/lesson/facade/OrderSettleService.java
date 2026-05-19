package jp.ne.yonem.restful.infrastructure.lesson.facade;

import java.util.Objects;
import org.springframework.stereotype.Service;

/** 注文業務を統括するサービスです。 */
@Service
public class OrderSettleService {

  /**
   * 注文を確定させます。
   *
   * @param facade 注文ファサード
   * @param productId 商品ID
   * @param price 価格
   * @return 実行結果
   */
  public boolean execute(OrderProcessorFacade facade, String productId, int price) {
    var safeFacade = Objects.requireNonNull(facade, "facade must not be null");
    return safeFacade.processOrder(productId, price);
  }
}
