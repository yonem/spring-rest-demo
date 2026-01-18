package jp.ne.yonem.restful.infrastructure.lesson;

import java.util.List;
import java.util.Optional;

/**
 * 注文リストに対してバリデーション、割引適用、集計を順次実行するエンジンです。 *
 *
 * <p>実装の要件:
 *
 * <ul>
 *   <li>1. 不正なアイテム（単価が0以下、または数量が0以下）は {@link Optional} を利用して除外すること。
 *   <li>2. 商品カテゴリごとに小計を算出（groupingBy）すること。
 *   <li>3. 割引ルールを以下の優先順位で適用すること:
 *       <ol>
 *         <li>{@link CategoryRateDiscount}: カテゴリごとの割合割引を最初に適用
 *         <li>{@link FixedAmountDiscount}: 全体からの定額割引を最後に適用
 *       </ol>
 *   <li>4. 各計算ステップ（初期合計、カテゴリ割引後、定額割引後）を {@link SettlementDetail} に記録すること。
 * </ul>
 */
public class OrderSettlementEngine {

  /**
   * 注文アイテムを表すデータ構造です。 * @param productId 商品ID
   *
   * @param quantity 数量
   * @param unitPrice 単価
   * @param category 商品カテゴリ（例: "FOOD", "ELECTRONICS"）
   */
  public record OrderItem(String productId, int quantity, int unitPrice, String category) {}

  /** 適用可能な割引ルールの封印型定義です。 */
  public sealed interface DiscountRule permits FixedAmountDiscount, CategoryRateDiscount {}

  /** 定額割引（例: 500円引き） */
  public record FixedAmountDiscount(int amount) implements DiscountRule {}

  /** カテゴリ別割合割引（例: FOODカテゴリは10%引き） */
  public record CategoryRateDiscount(String category, double rate) implements DiscountRule {}

  /** 計算の各ステップの詳細 */
  public record SettlementDetail(String description, int amountBefore, int amountAfter) {}

  /** 最終的な精算結果 */
  public record SettlementResult(int finalAmount, List<SettlementDetail> details) {}

  /**
   * 注文リストと割引ルールのリストを受け取り、精算結果を返します。 * @param items 注文アイテムのリスト（nullや不正なデータが含まれる可能性がある）
   *
   * @param rules 適用する割引ルールのリスト
   * @return 精算結果（明細付き）
   */
  public SettlementResult legacy(List<OrderItem> items, List<DiscountRule> rules) {
    // TODO: ここに実装を記述してください

    // ヒント:
    // 1. Stream でアイテムをクレンジング（Optional or filter）
    // 2. カテゴリごとに小計を出す
    // 3. ルールをソート（instanceof や型判定を利用）
    // 4. 計算過程を List<SettlementDetail> に add していく

    return null; // ダミー
  }
}
