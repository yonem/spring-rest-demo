package jp.ne.yonem.restful.infrastructure.lesson.command;

import java.util.Objects;

/** テキストを追加する具体的なコマンドクラスです。 */
public class AppendTextCommand implements EditorCommand {

  private final TextEditorReceiver receiver;
  private final String textToAppend;
  private String previousText = "";

  public AppendTextCommand(TextEditorReceiver receiver, String textToAppend) {
    this.receiver = Objects.requireNonNull(receiver, "receiver must not be null");
    this.textToAppend = Objects.requireNonNull(textToAppend, "textToAppend must not be null");
  }

  @Override
  public void execute() {
    // 実行前の状態をバックアップ（Undo用）
    this.previousText = receiver.getText();
    receiver.append(textToAppend);
  }

  @Override
  public void undo() {
    // 実行前の状態に書き戻す
    receiver.setText(previousText);
  }
}
