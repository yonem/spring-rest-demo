package jp.ne.yonem.restful.infrastructure.lesson2.factorymethod;

import java.util.Objects;
import org.springframework.stereotype.Service;

/** Factory Methodパターンを利用してレポート出力ワークフローを実行するサービスです。 */
@Service
public class ReportGenerationWorkflowService {

  /**
   * 指定されたレポート工場を使用してレポートを出力します。
   *
   * @param factory レポート工場
   * @param title レポートタイトル
   * @param content レポート本文
   * @return 出力結果文字列
   */
  public String execute(AbstractReportFactory factory, String title, String content) {
    var safeFactory = Objects.requireNonNull(factory, "factory must not be null");
    var safeTitle = Objects.requireNonNull(title, "title must not be null");
    var safeContent = Objects.requireNonNull(content, "content must not be null");

    return safeFactory.generate(safeTitle, safeContent);
  }
}
