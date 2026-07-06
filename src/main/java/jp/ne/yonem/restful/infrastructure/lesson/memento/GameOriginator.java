package jp.ne.yonem.restful.infrastructure.lesson.memento;

import java.util.Objects;
import lombok.Getter;

/** 現在のゲーム状態を保持し、メメントの生成と復元を行う生成者クラス */
@Getter
public class GameOriginator {
  private int hp;
  private int stage;

  public void changeState(int hp, int stage) {
    this.hp = hp;
    this.stage = stage;
  }

  /**
   * 現在の状態を保存したメメントを生成します Lights-out。
   *
   * @return 現在の状態のメメント
   */
  public GameMemento save() {
    return new GameMemento(this.hp, this.stage);
  }

  /**
   * 渡されたメメントから状態を復元します。
   *
   * @param memento 復元対象のメメント
   */
  public void restore(GameMemento memento) {
    var safeMemento = Objects.requireNonNull(memento, "memento must not be null");
    this.hp = safeMemento.hp();
    this.stage = safeMemento.stage();
  }
}
