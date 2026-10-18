package jp.ne.yonem.restful.infrastructure.lesson2.proxy;

/** レポートデータアクセスの共通インターフェースです。 */
public interface ReportDataReader {
  String readReport(String userRole);
}
