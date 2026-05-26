package jp.ne.yonem.restful.infrastructure.lesson.prototype;

import java.util.Objects;

/** レポートの構成情報を保持するRecordです。 */
public record ReportTemplate(String title, String header, String footer, String language) {

  /**
   * コピーコンストラクタを使用して、既存のインスタンスから新しいインスタンスを複製します。
   *
   * @param source 複製元
   * @return 複製された新しいインスタンス
   */
  public static ReportTemplate copyOf(ReportTemplate source) {
    var safeSource = Objects.requireNonNull(source, "source must not be null");
    return new ReportTemplate(
        safeSource.title(), safeSource.header(), safeSource.footer(), safeSource.language());
  }

  /**
   * 言語だけを変更したコピーを作成します。
   *
   * @param newLanguage 新しい言語
   * @return 言語変更後のインスタンス
   */
  public ReportTemplate withLanguage(String newLanguage) {
    var safeLang = Objects.requireNonNull(newLanguage, "language must not be null");
    return new ReportTemplate(this.title, this.header, this.footer, safeLang);
  }
}
