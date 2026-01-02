package jp.ne.yonem.restful.infrastructure;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ShippingCalculatorTest {

  private ShippingCalculator calculator;

  @BeforeEach
  void setUp() {
    calculator = new ShippingCalculator();
  }

  @Nested
  @DisplayName("正常系のテスト")
  class PositiveTests {

    @ParameterizedTest(name = "{0}便: 重量{1}kg のとき送料は{2}円")
    @CsvSource({
      "STANDARD,      10.0,  500", // 通常便は重量に関わらず一律
      "STANDARD,     100.0,  500",
      "EXPRESS,        1.0,  500", // 1.0 * 200 + 300 = 500
      "EXPRESS,        2.5,  800", // 2.5 * 200 + 300 = 800
      "INTERNATIONAL,  1.0, 3000", // 1.0 * 1000 + 2000 = 3000
      "INTERNATIONAL,  5.0, 7000" // 5.0 * 1000 + 2000 = 7000
    })
    void 送料が正しく計算されること(DeliveryType type, double weight, int expected) {
      assertThat(calculator.calculate(type, weight)).isEqualTo(expected);
    }
  }

  @Nested
  @DisplayName("異常系・境界値のテスト")
  class NegativeAndEdgeTests {

    @Test
    @DisplayName("配送方法に null を指定した場合、例外がスローされること")
    void testCalculateNullType() {
      assertThatThrownBy(() -> calculator.calculate(null, 1.0))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("未対応の配送方法です");
    }

    @Test
    @DisplayName("重量が 0kg の場合でも計算が通ること")
    void testCalculateZeroWeight() {
      // 急ぎ便の場合：0 * 200 + 300 = 300
      assertThat(calculator.calculate(DeliveryType.EXPRESS, 0.0)).isEqualTo(300);
    }
  }
}
