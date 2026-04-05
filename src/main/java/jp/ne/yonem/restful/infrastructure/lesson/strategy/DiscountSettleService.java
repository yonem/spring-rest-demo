package jp.ne.yonem.restful.infrastructure.lesson.strategy;

import java.util.Objects;
import org.springframework.stereotype.Service;

/** 会員ランクに応じた割引精算を行うサービスです。 */
@Service
public class DiscountSettleService {

  /**
   * 会員ランクに基づいて割引後の金額を算出します。
   *
   * @param rank 会員ランク
   * @param price 元の金額
   * @return 割引後の金額
   */
  public int execute(MemberRank rank, int price) {
    var selectedRank = Objects.requireNonNullElse(rank, MemberRank.STANDARD);
    return selectedRank.applyDiscount(price);
  }
}
