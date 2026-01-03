package jp.ne.yonem.restful.application.discount;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;
import jp.ne.yonem.restful.application.discount.DiscountService.AmountDiscount;
import jp.ne.yonem.restful.application.discount.DiscountService.DiscountRule;
import jp.ne.yonem.restful.application.discount.DiscountService.PercentageDiscount;
import jp.ne.yonem.restful.application.discount.DiscountService.SeasonalDiscount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DiscountServiceTest {
  private DiscountService service;

  @BeforeEach
  void setUp() {
    service = new DiscountService();
  }

  @Nested
  @DisplayName("割引ルールの計算テスト")
  class DiscountCalculationTests {

    @ParameterizedTest(name = "{0} のとき、元の価格 {1}円 が {2}円 になること")
    @MethodSource("discountProvider")
    void testApplyDiscount(DiscountRule rule, int originalPrice, int expectedPrice) {
      var result = service.execute(originalPrice, rule);
      assertThat(result).isEqualTo(expectedPrice);
    }

    // 複雑なオブジェクトをパラメータとして渡すための MethodSource
    static Stream<Arguments> discountProvider() {
      return Stream.of(
          // AmountDiscount: 定額引き
          Arguments.of(new AmountDiscount(1000), 5000, 4000),
          Arguments.of(new AmountDiscount(1000), 500, 0), // 0円以下にならないこと

          // PercentageDiscount: 20%引き
          Arguments.of(new PercentageDiscount(0.2), 10000, 8000),
          Arguments.of(new PercentageDiscount(1.0), 10000, 0),

          // SeasonalDiscount: 夏はさらにボーナス引き
          Arguments.of(
              new SeasonalDiscount("SUMMER", 1000), 5000, 3500), // 5000 - 1000 - 500(bonus)
          Arguments.of(new SeasonalDiscount("WINTER", 1000), 5000, 4000) // 5000 - 1000
          );
    }
  }

  @Nested
  @DisplayName("異常系のテスト")
  class NegativeTests {
    @Test
    @DisplayName("nullのルールが渡された場合に適切にハンドリングされること")
    void shouldThrowExceptionWhenRuleIsNull() {
      assertThatThrownBy(() -> service.execute(1000, null))
          .isInstanceOf(NullPointerException.class);
      // switch式でnullを扱う場合は、case null を書かない限りNPEになります
    }
  }
}
