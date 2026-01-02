package jp.ne.yonem.restful.infrastructure;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class ShippingCalculator {

  // EnumMapを使用：メモリ効率とアクセス速度が最大化される
  private static final Map<DeliveryType, Function<Double, Integer>> STRATEGIES;

  static {
    STRATEGIES = new EnumMap<>(DeliveryType.class);

    // 通常便：一律 500円
    STRATEGIES.put(DeliveryType.STANDARD, weight -> 500);

    // 急ぎ便：重量(kg) × 200円 + 基本料 300円
    STRATEGIES.put(DeliveryType.EXPRESS, weight -> (int) (weight * 200) + 300);

    // 国際郵便：重量(kg) × 1000円 + 基本料 2000円
    STRATEGIES.put(DeliveryType.INTERNATIONAL, weight -> (int) (weight * 1000) + 2000);
  }

  /**
   * 送料を計算します。
   *
   * @param type 配送方法
   * @param weight 重量
   * @return 送料
   */
  public int calculate(DeliveryType type, double weight) {
    return Optional.ofNullable(type)
        .map(STRATEGIES::get) // Mapから計算式(Function)を取得
        .map(strategy -> strategy.apply(weight)) // 計算式を実行
        .orElseThrow(() -> new IllegalArgumentException("未対応の配送方法です"));
  }
}
