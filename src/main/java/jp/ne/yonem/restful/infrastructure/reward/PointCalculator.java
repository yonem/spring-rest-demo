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

  /** 計算の各ステップにおける詳細を記録する Record */
  public record CalculationStep(String ruleName, double pointsAtStep) {}

  /** 最終的な計算結果と、その根拠となる明細を保持する Record */
  public record PointCalculationResult(int finalPoints, List<CalculationStep> details) {}

  /**
   * 指定された金額に対し、複数の報酬ルールを適用して最終ポイントと計算明細を算出します。
   *
   * <p>このメソッドは以下の手順で計算を実行します：
   *
   * <ol>
   *   <li>入力された全ルールを {@link RewardPriority} に定義された優先順位に従ってソートします。
   *   <li>ソートされた各ルールを順次適用し、適用後の累計ポイントを {@link CalculationStep} として記録します。
   *   <li>すべてのルールの適用が完了した後、最終的な整数値ポイントと全計算ステップを含む結果オブジェクトを返します。
   * </ol>
   *
   * <p><b>計算順序の重要性:</b> 優先順位（Priority）が低いルールほど先に適用されるため、 「基本付与（加算）」の後に「ランク倍率（乗算）」を適用するといったビジネスルールが
   * プログラム構造として保証されます。
   *
   * @param amount 購入金額。基本ポイントの算出基礎となります。
   * @param rules 適用候補となるルールのリスト。空のリストを指定した場合は 0 ポイントが返されます。
   * @return 計算結果と監査用明細を含む {@link PointCalculationResult} オブジェクト。
   * @throws NullPointerException rules またはその要素に null が含まれる場合。
   * @see RewardRule
   * @see RewardPriority
   * @see PointCalculationResult
   */
  public PointCalculationResult calculate(int amount, List<RewardRule> rules) {

    // 1. 優先順位でソート
    var sortedRules =
        rules.stream().sorted(Comparator.comparingInt(r -> r.priority().getValue())).toList();

    // 2. 計算と履歴の蓄積
    var details = new ArrayList<CalculationStep>();
    var currentPoints = 0.0;

    for (var rule : sortedRules) {
      currentPoints = applyRule(amount, currentPoints, rule);

      // ルールの名前と、適用後のポイントを記録
      details.add(new CalculationStep(rule.getClass().getSimpleName(), currentPoints));
    }
    return new PointCalculationResult((int) currentPoints, List.copyOf(details));
  }

  private double applyRule(int amount, double currentPoints, RewardRule rule) {
    return switch (rule) {
      case BasePoint(var rate) -> currentPoints + (amount / 100.0 * rate);
      case CampaignPoint(var bonus) -> currentPoints + bonus;
      case RankMultiplier(var factor) -> currentPoints * factor;
    };
  }
}
