package jp.ne.yonem.restful.infrastructure.lesson2.prototype;

import lombok.Data;

/** ドキュメントスタイル（雛形）を表す具象プロトタイプクラスです。 */
@Data
public class DocumentStylePrototype implements Prototype<DocumentStylePrototype> {

  private String fontName;
  private int fontSize;
  private String themeColor;

  public DocumentStylePrototype(String fontName, int fontSize, String themeColor) {
    this.fontName = fontName;
    this.fontSize = fontSize;
    this.themeColor = themeColor;
  }

  // 複製用のコピーコンストラクタ
  private DocumentStylePrototype(DocumentStylePrototype target) {
    if (target != null) {
      this.fontName = target.fontName;
      this.fontSize = target.fontSize;
      this.themeColor = target.themeColor;
    }
  }

  @Override
  public DocumentStylePrototype clone() {
    return new DocumentStylePrototype(this);
  }
}
