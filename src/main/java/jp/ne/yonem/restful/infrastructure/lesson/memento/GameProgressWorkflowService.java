package jp.ne.yonem.restful.infrastructure.lesson.memento;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;

/** ゲームの進行状態とセーブポイント（メメント）の管理を行うサービス */
@Service
public class GameProgressWorkflowService {

  private final Deque<GameMemento> savePoints = new ArrayDeque<>();

  /**
   * 現在のゲーム状態をセーブポイントに保存する
   *
   * @param originator ゲーム進行体
   */
  public void saveCheckpoint(GameOriginator originator) {
    var safeOriginator = Objects.requireNonNull(originator, "originator must not be null");
    this.savePoints.push(safeOriginator.save());
  }

  /**
   * 直前のセーブポイントの状態に復元する
   *
   * @param originator ゲーム進行体
   */
  public void loadLastCheckpoint(GameOriginator originator) {
    var safeOriginator = Objects.requireNonNull(originator, "originator must not be null");
    Optional.ofNullable(this.savePoints.poll()).ifPresent(safeOriginator::restore);
  }
}
