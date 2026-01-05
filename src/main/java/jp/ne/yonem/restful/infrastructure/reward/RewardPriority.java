package jp.ne.yonem.restful.infrastructure.reward;

import lombok.Getter;

@Getter
public enum RewardPriority {
  BASE(10), // 最初：基本付与
  CAMPAIGN(20), // 中間：特定のキャンペーン
  MULTIPLIER(30); // 最後：会員ランクによる倍率

  private final int value;

  RewardPriority(int value) {
    this.value = value;
  }
}
