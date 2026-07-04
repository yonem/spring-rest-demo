package jp.ne.yonem.restful.infrastructure.lesson.command;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import org.springframework.stereotype.Service;

/** コマンドの実行および履歴（Undo）を管理・検証するサービスです。 */
@Service
public class CommandHistoryWorkflowService {

  // コマンドの実行履歴を保持するスタック
  private final Deque<EditorCommand> history = new ArrayDeque<>();

  /**
   * 指定されたコマンドを実行し、履歴に記録します。
   *
   * @param command 実行するコマンド
   */
  public void executeCommand(EditorCommand command) {
    var safeCommand = Objects.requireNonNull(command, "command must not be null");
    safeCommand.execute();
    history.push(safeCommand);
  }

  /** 直前に実行したコマンドを取り消します（Undo）。 */
  public void undoLastCommand() {
    // Optionalを活用して安全にポップ
    var lastCommand = java.util.Optional.ofNullable(history.poll());
    lastCommand.ifPresent(EditorCommand::undo);
  }
}
