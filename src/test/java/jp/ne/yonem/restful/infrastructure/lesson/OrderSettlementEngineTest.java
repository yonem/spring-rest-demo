package jp.ne.yonem.restful.infrastructure.lesson;

import static jp.ne.yonem.restful.infrastructure.lesson.OrderSettlementEngine.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderSettlementEngineTest {

  private OrderSettlementEngine engine;

  @BeforeEach
  void setUp() {
    engine = new OrderSettlementEngine();
  }

  @Test
  @DisplayName("正常系：カテゴリ割引と定額割引が正しい順序で適用されること")
  void process_SuccessPath() {

    // 準備
    // FOOD: 1000円 * 2 = 2000円 (10%引き対象 -> 1800円)
    // ELECT: 5000円 * 1 = 5000円 (割引対象外)
    // 合計: 6800円。そこから定額 800円引き -> 6000円
    var items =
        List.of(new OrderItem("P1", 2, 1000, "FOOD"), new OrderItem("P2", 1, 5000, "ELECTRONICS"));
    List<DiscountRule> rules =
        List.of(new FixedAmountDiscount(800), new CategoryRateDiscount("FOOD", 0.1));

    // 実行
    var result = engine.modern(items, rules);

    // 検証
    assertThat(result.finalAmount()).isEqualTo(6000);
    assertThat(result.details()).hasSize(3); // 初期, カテゴリ割引後, 定額割引後

    assertThat(result.details().get(1).description()).contains("カテゴリ割引");
    assertThat(result.details().get(1).amountAfter()).isEqualTo(6800);

    assertThat(result.details().get(2).description()).contains("定額割引");
    assertThat(result.details().get(2).amountAfter()).isEqualTo(6000);
  }

  @Test
  @DisplayName("異常系：不正なアイテム（単価0以下など）が除外されて計算されること")
  void process_ExcludeInvalidItems() {
    var items = new ArrayList<OrderItem>();
    items.add(new OrderItem("Valid", 1, 1000, "FOOD"));
    items.add(new OrderItem("InvalidPrice", 1, -500, "FOOD"));
    items.add(null); // null要素
    items.add(new OrderItem("InvalidQty", 0, 1000, "FOOD"));
    items.add(new OrderItem(null, 1, 1000, null));

    var result = engine.modern(items, List.of()); // 割引なし

    // 有効な 1000円分だけが計上されていること
    assertThat(result.finalAmount()).isEqualTo(1000);
  }

  @Test
  @DisplayName("境界値：割引額が合計を超えた場合、最終金額が0円になること")
  void process_AmountShouldNotBeNegative() {
    var items = List.of(new OrderItem("P1", 1, 1000, "FOOD"));
    List<DiscountRule> rules = List.of(new FixedAmountDiscount(5000)); // 合計より大きい割引

    var result = engine.modern(items, rules);

    assertThat(result.finalAmount()).isEqualTo(0);
  }
}
