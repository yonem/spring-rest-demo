package jp.ne.yonem.restful.infrastructure.lesson2.prototype;

import java.util.Objects;
import org.springframework.stereotype.Service;

/** Prototypeパターンを利用してスタイル雛形を複製・調整するサービスです。 */
@Service
public class DocumentStyleWorkflowService {

  /**
   * 指定したキーの雛形を複製し、フォントサイズを変更して適用結果を返します。
   *
   * @param registry プロトタイプレジストリ
   * @param styleKey 雛形キー
   * @param customFontSize 調整後のフォントサイズ
   * @return スタイル適用結果サマリー
   */
  public String execute(DocumentStyleRegistry registry, String styleKey, int customFontSize) {
    var safeRegistry = Objects.requireNonNull(registry, "registry must not be null");
    var safeKey = Objects.requireNonNull(styleKey, "styleKey must not be null");

    if (customFontSize <= 0) {
      throw new IllegalArgumentException("customFontSize must be greater than 0");
    }

    // 元オブジェクトを変更せず、複製（Clone）を取得してカスタマイズ
    var clonedStyle = safeRegistry.getCloned(safeKey);
    clonedStyle.setFontSize(customFontSize);

    return "Style[%s] Font:%s Size:%d Color:%s"
        .formatted(
            safeKey,
            clonedStyle.getFontName(),
            clonedStyle.getFontSize(),
            clonedStyle.getThemeColor());
  }
}
