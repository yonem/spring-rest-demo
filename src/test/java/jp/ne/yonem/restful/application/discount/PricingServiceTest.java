package jp.ne.yonem.restful.application.discount;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PricingServiceTest {
  private PricingService sut;

  @BeforeEach
  void setUp() {
    sut = new PricingService();
  }

  @Nested
  @DisplayName("calculate メソッドのテスト（リファクタリング後）")
  class CalculateTest {

    @ParameterizedTest(name = "{0}, キャンペーン:{1}, 元値:{2}円 -> 期待値:{3}円")
    @MethodSource("providePriceScenarios")
    @DisplayName("各種割引条件が正しく適用されること")
    void testCalculate(String type, String code, int price, int expected) {
      var actual = sut.calculate(type, price, code);
      assertEquals(expected, actual);
    }

    private static Stream<Arguments> providePriceScenarios() {
      return Stream.of(
          // 割引なし
          Arguments.of("NORMAL", "NONE", 10000, 10000),
          // 顧客割引のみ (VIP: 20% OFF)
          Arguments.of("VIP", null, 10000, 8000),
          // 顧客割引のみ (PREMIUM: 10% OFF)
          Arguments.of("PREMIUM", null, 10000, 9000),
          // キャンペーン割引のみ (WELCOME: 500円引き)
          Arguments.of(null, "WELCOME", 1000, 500),
          // 重複適用 (VIP: 20% OFF -> WINTER_2026: 1000円引き)
          // 10000 * 0.8 = 8000, 8000 - 1000 = 7000
          Arguments.of("VIP", "WINTER_2026", 10000, 7000),
          // 割引の結果、価格がマイナスになる場合は0
          Arguments.of(null, "WINTER_2026", 500, 0));
    }
  }

  @Nested
  @DisplayName("calculateFinalPrice メソッドのテスト（レガシー版）")
  class CalculateFinalPriceTest {

    @ParameterizedTest(name = "{0}, キャンペーン:{1}, 元値:{2}円 -> 期待値:{3}円")
    @MethodSource("providePriceScenarios")
    @DisplayName("旧ロジックでも期待通り計算されること")
    void testCalculateFinalPrice(String type, String code, int price, int expected) {
      var actual = sut.calculateFinalPrice(type, price, code);
      assertEquals(expected, actual);
    }

    private static Stream<Arguments> providePriceScenarios() {
      // 基本的に同一のテストデータを利用
      return CalculateTest.providePriceScenarios();
    }
  }
}
