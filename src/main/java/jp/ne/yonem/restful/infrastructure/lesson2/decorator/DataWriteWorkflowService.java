package jp.ne.yonem.restful.infrastructure.lesson2.decorator;

import java.util.Objects;
import org.springframework.stereotype.Service;

/** Decoratorパターンを利用してデータ書き込み処理を実行するサービスです。 */
@Service
public class DataWriteWorkflowService {

  /**
   * 指定されたデコレーター構成でデータを書き込みます。
   *
   * @param writer データライター（Decorator含む）
   * @param data 書き込む文字列
   * @return 加工後の結果文字列
   */
  public String execute(DataWriter writer, String data) {
    var safeWriter = Objects.requireNonNull(writer, "writer must not be null");
    var safeData = Objects.requireNonNull(data, "data must not be null");

    return safeWriter.writeData(safeData);
  }
}
