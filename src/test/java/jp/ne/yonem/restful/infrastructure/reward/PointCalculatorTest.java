package jp.ne.yonem.restful.infrastructure.reward;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import jp.ne.yonem.restful.infrastructure.reward.PointCalculator.BasePoint;
import jp.ne.yonem.restful.infrastructure.reward.PointCalculator.RankMultiplier;
import jp.ne.yonem.restful.infrastructure.reward.PointCalculator.RewardRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PointCalculatorTest {

  private PointCalculator calculator;

  @BeforeEach
  void setUp() {
    calculator = new PointCalculator();
  }

  @Test
  @DisplayName("優先順位に従って正しく計算されること (加算 -> 倍率)")
  void calculate_ShouldFollowPriority() {

    // 1000円、基本1%(=10pt)、ボーナス50pt、倍率2倍
    // 期待値: (10 + 50) * 2 = 120pt
    // ※ もし倍率が先に計算されると (0 * 2) + 10 + 50 = 60pt になり失敗する
    List<RewardRule> rules =
        List.of(
            new RankMultiplier(2.0), // 優先度高(30)
            new BasePoint(1), // 優先度低(10)
            new PointCalculator.CampaignPoint(50) // 優先度中(20)
            );

    var result = calculator.calculate(1000, rules);

    assertThat(result).isEqualTo(120);
  }

  @Nested
  @DisplayName("ルール組み合わせのテスト")
  class CombinationTests {

    @Test
    @DisplayName("ルールが空の場合は0ポイントを返すこと")
    void calculate_EmptyRules_ShouldReturnZero() {
      var result = calculator.calculate(5000, List.of());
      assertThat(result).isZero();
    }

    @Test
    @DisplayName("複数の倍率ルールがある場合、順次適用されること")
    void calculate_MultipleMultipliers_ShouldBeCumulative() {

      // 1000円、基本1%(=10pt)、2倍、さらに1.5倍
      // 期待値: 10 * 2.0 * 1.5 = 30pt
      List<RewardRule> rules =
          List.of(new BasePoint(1), new RankMultiplier(2.0), new RankMultiplier(1.5));

      var result = calculator.calculate(1000, rules);
      assertThat(result).isEqualTo(30);
    }
  }
}
