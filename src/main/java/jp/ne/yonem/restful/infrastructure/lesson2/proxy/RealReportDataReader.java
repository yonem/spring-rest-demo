package jp.ne.yonem.restful.infrastructure.lesson2.proxy;

import java.util.Objects;

/** 実際に重いデータのロードを行う本物クラス（Real Subject）です。 */
public class RealReportDataReader implements ReportDataReader {

  private final String reportName;

  public RealReportDataReader(String reportName) {
    this.reportName = Objects.requireNonNull(reportName, "reportName must not be null");
    // 重いロード処理を模倣
    loadFromDatabase();
  }

  private void loadFromDatabase() {
    // データベースやファイルからの読み込みコストが発生する想定
  }

  @Override
  public String readReport(String userRole) {
    return "ReportData[%s]".formatted(reportName);
  }
}
