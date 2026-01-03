package jp.ne.yonem.restful.application.discount;

import org.springframework.stereotype.Service;

/** DOPサンプルサービス */
@Service
public class DiscountService {
  public sealed interface DiscountRule
      permits AmountDiscount, PercentageDiscount, SeasonalDiscount {}

  // 定額割引
  public record AmountDiscount(int amount) implements DiscountRule {}

  // 割合割引
  public record PercentageDiscount(double rate) implements DiscountRule {}

  // 季節割引（期間と割引額）
  public record SeasonalDiscount(String season, int amount) implements DiscountRule {}

  public int execute(int originalPrice, DiscountRule rule) {

    return switch (rule) {
      case AmountDiscount(var amount) -> Math.max(0, originalPrice - amount);
      case PercentageDiscount(var rate) -> (int) (originalPrice * (1.0 - rate));
      case SeasonalDiscount(var season, var amount) -> {
        var bonus = season.equals("SUMMER") ? 500 : 0;
        yield Math.max(0, originalPrice - amount - bonus);
      }
        // 補足: sealed interface なので、すべてのケースを網羅していれば default は不要（コンパイラがチェックしてくれる）
    };
  }
}
