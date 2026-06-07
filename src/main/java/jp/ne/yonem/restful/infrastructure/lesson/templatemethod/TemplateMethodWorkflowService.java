package jp.ne.yonem.restful.infrastructure.lesson.templatemethod;

import java.util.Objects;
import org.springframework.stereotype.Service;

/** テンプレートメソッドパターンを検証・実行するサービスです。 */
@Service
public class TemplateMethodWorkflowService {

  /**
   * 指定されたプロセッサーを使用して、一連のデータ処理ワークフローを実行します。
   *
   * @param processor データプロセッサー
   * @param source 処理対象データ
   * @return 最終加工データ
   */
  public String execute(AbstractDataProcessor processor, String source) {
    var safeProcessor = Objects.requireNonNull(processor, "processor must not be null");
    var safeSource = Objects.requireNonNull(source, "source must not be null");

    return safeProcessor.process(safeSource);
  }
}
