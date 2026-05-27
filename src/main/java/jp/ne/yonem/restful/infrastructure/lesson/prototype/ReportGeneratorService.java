package jp.ne.yonem.restful.infrastructure.lesson.prototype;

import java.util.Objects;
import org.springframework.stereotype.Service;

/** プロトタイプパターンを使用してレポートを生成するサービスです。 */
@Service
public class ReportGeneratorService {

  /**
   * 雛形となるレポートを複製し、指定された言語に適合させて発行します。
   *
   * @param baseTemplate 雛形
   * @param targetLanguage 対象言語
   * @return カスタマイズされたレポート
   */
  public ReportTemplate execute(ReportTemplate baseTemplate, String targetLanguage) {
    var safeTemplate = Objects.requireNonNull(baseTemplate, "baseTemplate must not be null");
    var safeLanguage = Objects.requireNonNull(targetLanguage, "targetLanguage must not be null");

    // 雛形をコピーして一部（言語）だけを書き換える
    return safeTemplate.withLanguage(safeLanguage);
  }
}
