package jp.ne.yonem.restful.infrastructure.lesson.command;

/** コマンド（命令）の共通インターフェースです。 */
public interface EditorCommand {
  void execute();

  void undo();
}
