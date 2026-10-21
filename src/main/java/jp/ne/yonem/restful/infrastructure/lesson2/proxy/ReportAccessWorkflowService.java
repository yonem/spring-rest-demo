package jp.ne.yonem.restful.infrastructure.lesson2.proxy;

import java.util.Objects;
import org.springframework.stereotype.Service;

/** Proxyパターンを利用して安全にレポートアクセスを行うサービスです。 */
@Service
public class ReportAccessWorkflowService {

  /**
   * 指定されたリーダーとユーザーロールでレポート閲覧処理を実行します。
   *
   * @param reader データリーダー（Proxy含む）
   * @param userRole ユーザーのロール
   * @return レポート閲覧結果
   */
  public String execute(ReportDataReader reader, String userRole) {
    var safeReader = Objects.requireNonNull(reader, "reader must not be null");
    var safeUserRole = Objects.requireNonNull(userRole, "userRole must not be null");

    return safeReader.readReport(safeUserRole);
  }
}
