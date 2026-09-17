package jp.ne.yonem.restful.infrastructure.lesson2.composite;

import java.util.Objects;
import org.springframework.stereotype.Service;

/** Compositeパターンを利用してカタログ構造の価格集計を行うサービスです。 */
@Service
public class CatalogPriceCalculationService {

  /**
   * 指定されたカタログ要素（単品商品またはカテゴリー）の合計価格を計算します。
   *
   * @param component 計算対象のカタログ要素
   * @return 合計価格
   */
  public int execute(CatalogComponent component) {
    var safeComponent = Objects.requireNonNull(component, "component must not be null");
    return safeComponent.price();
  }
}
