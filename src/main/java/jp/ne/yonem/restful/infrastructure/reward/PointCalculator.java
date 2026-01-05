package jp.ne.yonem.restful.infrastructure.reward;

import java.util.*;

/**
 * 報酬ポイントを計算するエンジンクラスです。
 *
 * <p>このクラスは以下のパラダイムに基づいて設計されています：
 *
 * <ul>
 *   <li><b>データ指向プログラミング:</b> ルールを {@link RewardRule} というデータ形状で扱い、 振る舞い（計算ロジック）を Pattern Matching
 *       で分離しています。
 *   <li><b>宣言的な優先順位制御:</b> ルールの適用順序を {@link RewardPriority} に基づき自動ソートします。
 *       これにより、計算順序の依存関係（例：加算後に倍率を適用する）を型安全に保証します。
 *   <li><b>不変性の維持:</b> Stream API の reduce を使用し、計算過程での副作用を排除しています。
 * </ul>
 */
public class PointCalculator {
  public sealed interface RewardRule permits BasePoint, CampaignPoint, RankMultiplier {
    RewardPriority priority();
  }

  // 基本：100円につき1ポイント
  public record BasePoint(int ratePerYen) implements RewardRule {
    public RewardPriority priority() {
      return RewardPriority.BASE;
    }
  }

  // キャンペーン：一律 +50ポイント
  public record CampaignPoint(int bonus) implements RewardRule {
    public RewardPriority priority() {
      return RewardPriority.CAMPAIGN;
    }
  }

  // 倍率：合計を2倍にする
  public record RankMultiplier(double factor) implements RewardRule {
    public RewardPriority priority() {
      return RewardPriority.MULTIPLIER;
    }
  }

  /**
   * 指定された金額とルールのリストに基づいて最終的な獲得ポイントを計算します。
   *
   * @param amount 購入金額
   * @param rules 適用するルールのリスト（順不同）
   * @return 計算済みの最終ポイント（小数点以下切り捨て）
   * @throws NullPointerException rules または要素が null の場合
   */
  public int calculate(int amount, List<RewardRule> rules) {

    // 1. 優先順位（priority）の低い順にソート
    var sortedRules =
        rules.stream().sorted(Comparator.comparingInt(r -> r.priority().getValue())).toList();

    // 2. 順次適用（reduce で状態を回す）
    var finalPoints =
        sortedRules.stream()
            .reduce((double) 0, (current, rule) -> applyRule(amount, current, rule), Double::sum);

    return finalPoints.intValue();
  }

  private double applyRule(int amount, double currentPoints, RewardRule rule) {
    return switch (rule) {
      case BasePoint(var rate) -> currentPoints + (amount / 100.0 * rate);
      case CampaignPoint(var bonus) -> currentPoints + bonus;
      case RankMultiplier(var factor) -> currentPoints * factor;
    };
  }
}
