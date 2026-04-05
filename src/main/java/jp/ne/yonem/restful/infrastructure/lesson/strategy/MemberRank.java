package jp.ne.yonem.restful.infrastructure.lesson.strategy;

import java.util.function.IntUnaryOperator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** 会員ランクとそれぞれの割引戦略を定義する列挙型です。 */
@Getter
@RequiredArgsConstructor
public enum MemberRank {
  GOLD(price -> (int) (price * 0.8)),
  SILVER(price -> (int) (price * 0.9)),
  STANDARD(price -> price);

  // Java標準の「intを入力してintを返す」関数型インターフェースを使用
  private final IntUnaryOperator discountStrategy;

  /**
   * 指定された金額に割引を適用します。
   *
   * @param price 元の金額
   * @return 割引後の金額
   */
  public int applyDiscount(int price) {
    return discountStrategy.applyAsInt(price);
  }
}
