package jp.ne.yonem.restful.infrastructure.lesson.command;

import java.util.Objects;
import lombok.Getter;

/** 実際にテキストを保持・操作する受信者クラスです。 */
@Getter
public class TextEditorReceiver {
  private String text = "";

  public void append(String textToAppend) {
    this.text = this.text + Objects.requireNonNull(textToAppend, "textToAppend must not be null");
  }

  public void setText(String text) {
    this.text = Objects.requireNonNull(text, "text must not be null");
  }
}
