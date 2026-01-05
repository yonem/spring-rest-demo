package jp.ne.yonem.restful.application.discount;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

/**
 * リファクタリング戦略：役割の分離
 *
 * <ul>
 *   <li>データの構造化: 割引条件を {@code record} と {@code sealed interface} で定義
 *   <li>ロジックのデータ化: 固定的な割引を {@code Map} に追い出す
 *   <li>計算プロセスの宣言化: {@code Stream} と {@code Pattern Matching} でパイプライン処理にする
 * </ul>
 */
@Service
public class PricingService {
  public sealed interface Discount permits CustomerDiscount, CampaignDiscount {}

  public record CustomerDiscount(double rate) implements Discount {}

  public record CampaignDiscount(int amount) implements Discount {}

  /** 顧客タイプごとの割引率をMapで管理（O(1)アクセス & メモリ効率） */
  private static final Map<String, Discount> CUSTOMER_RULES =
      Map.of(
          "VIP", new CustomerDiscount(0.2),
          "PREMIUM", new CustomerDiscount(0.1));

  /** キャンペーンは変動が多いため、必要に応じて外部から注入・取得 */
  private static final Map<String, Discount> CAMPAIGN_RULES =
      Map.of(
          "WELCOME", new CampaignDiscount(500),
          "WINTER_2026", new CampaignDiscount(1000));

  /**
   * 👼 Good: 現代的なデータ指向コード
   *
   * @param type 値引きタイプ
   * @param price 価格
   * @param code キャンペーンコード
   * @return 算出価格
   */
  public int calculate(String type, int price, String code) {

    // 適用するルールをリスト化（何をやるかを並べる）
    var activeDiscounts =
        Stream.of(
                Optional.ofNullable(type).map(CUSTOMER_RULES::get),
                Optional.ofNullable(code).map(CAMPAIGN_RULES::get))
            .flatMap(Optional::stream)
            .toList();

    // 蓄積計算（減少させていく）
    var finalPrice = activeDiscounts.stream().reduce((double) price, this::applyRule, Double::sum);

    return Math.max(0, finalPrice.intValue());
  }

  /**
   * 計算ロジックを {@code Pattern Matching} でクリーンに分離
   *
   * @param current 現在価格
   * @param rule 値引きルール
   * @return 値引き価格
   */
  private double applyRule(double current, Discount rule) {
    return switch (rule) {
      case CustomerDiscount(var rate) -> current * (1.0 - rate);
      case CampaignDiscount(var amount) -> current - amount;
    };
  }

  /**
   * 👿 Bad: 混沌とした命令型コード
   *
   * @param customerType 値引きタイプ
   * @param price 価格
   * @param campaignCode キャンペーンコード
   * @return 算出価格
   */
  public int calculateFinalPrice(String customerType, int price, String campaignCode) {
    int result = price;

    // 顧客タイプによる割引（複雑なif-else）
    if ("VIP".equals(customerType)) {
      result *= 0.8;
    } else if ("PREMIUM".equals(customerType)) {
      result *= 0.9;
    }

    // キャンペーンコードによる減額（switch文のメンテナンス漏れが発生しやすい）
    if (campaignCode != null) {
      switch (campaignCode) {
        case "WELCOME":
          result -= 500;
          break;
        case "WINTER_2026":
          result -= 1000;
          break;
          // ...さらに増え続ける
      }
    }

    // 最終的な0円チェック
    return Math.max(0, result);
  }
}
