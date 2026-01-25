package jp.ne.yonem.restful.infrastructure.lesson;

import java.util.function.Function;

/** 複雑なビジネスロジックの「カプセル化」 */
public class CalculateShippingFee {

  public int legacy(String method, int weight) {
    if ("STANDARD".equals(method)) {
      return weight * 100;
    } else if ("EXPRESS".equals(method)) {
      return weight * 200 + 500;
    } else if ("INTERNATIONAL".equals(method)) {
      if (weight > 10) {
        return weight * 500 + 2000;
      } else {
        return weight * 500 + 1000;
      }
    } else {
      throw new IllegalArgumentException("Unknown method");
    }
  }

  public enum ShippingMethod {
    STANDARD(weight -> weight * 100),
    EXPRESS(weight -> weight * 200 + 500),
    INTERNATIONAL(weight -> weight * 500 + (10 < weight ? 2000 : 1000));

    private final Function<Integer, Integer> calculator;

    ShippingMethod(Function<Integer, Integer> calculator) {
      this.calculator = calculator;
    }

    public int calculate(int weight) {
      return calculator.apply(weight);
    }
  }

  /** Enum に「計算ロジック」を閉じ込める（Strategyパターン） */
  public int modern(String method, int weight) {
    return ShippingMethod.valueOf(method.toUpperCase()).calculate(weight);
  }

  /** Java 21+ の「Pattern Matching for switch」を使う */
  public int modern2(String method, int weight) {
    return switch (method.toUpperCase()) {
      case "STANDARD" -> weight * 100;
      case "EXPRESS" -> weight * 200 + 500;
      case "INTERNATIONAL" -> weight * 500 + (10 < weight ? 2000 : 1000);
      default -> throw new IllegalArgumentException("Unknown method: " + method);
    };
  }
}
