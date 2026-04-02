package jp.ne.yonem.restful.infrastructure.lesson.strategy;

import java.util.Objects;

public class DiscountService {

  public int legacy(String rank, int price) {
    if ("GOLD".equals(rank)) {
      return (int) (price * 0.8);
    } else if ("SILVER".equals(rank)) {
      return (int) (price * 0.9);
    } else {
      return price;
    }
  }

  /** 割引計算の戦略を定義するインターフェースです。 */
  @FunctionalInterface
  public interface DiscountStrategy {
    int apply(int price);
  }

  /**
   * ランクに基づき割引適用後の金額を算出します。
   *
   * @param rank 会員ランク (GOLD, SILVER, その他)
   * @param price 元の金額
   * @return 割引後の金額
   */
  public int execute(String rank, int price) {
    var strategy = selectStrategy(rank);
    return strategy.apply(price);
  }

  private DiscountStrategy selectStrategy(String rank) {
    return switch (Objects.requireNonNullElse(rank, "STANDARD")) {
      case "GOLD" -> p -> (int) (p * 0.8);
      case "SILVER" -> p -> (int) (p * 0.9);
      default -> p -> p;
    };
  }
}
