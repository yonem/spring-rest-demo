package jp.ne.yonem.restful.infrastructure.lesson2.proxy;

import java.util.Objects;

/** アクセス制御と遅延インスタンス化を担う代理クラス（Proxy）です。 */
public class ProxyReportDataReader implements ReportDataReader {

  private final String reportName;
  private RealReportDataReader realReader; // 呼び出されるまでインスタンス化を遅延（Virtual Proxy）

  public ProxyReportDataReader(String reportName) {
    this.reportName = Objects.requireNonNull(reportName, "reportName must not be null");
  }

  @Override
  public String readReport(String userRole) {
    var safeRole = Objects.requireNonNull(userRole, "userRole must not be null");

    // 1. Protection Proxy: アクセス権限チェック
    if (!"ADMIN".equalsIgnoreCase(safeRole)) {
      return "ACCESS DENIED: Required ADMIN role";
    }

    // 2. Virtual Proxy: 必要になって初めて本物を生成（遅延初期化）
    if (realReader == null) {
      realReader = new RealReportDataReader(reportName);
    }

    return realReader.readReport(safeRole);
  }
}
