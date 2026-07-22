package jp.ne.yonem.restful.infrastructure.lesson.visitor;

import java.util.Objects;
import org.springframework.stereotype.Service;

/** ビジターパターンによる構造解析・処理を統括するサービスです。 */
@Service
public class VisitorStructureAnalysisService {

  /**
   * 対象の要素構造に対して指定のビジターを実行し、集計されたサイズを返します。
   *
   * @param element 走査対象のデータ要素
   * @param visitor 適用する訪問者
   * @return 集計された総サイズ
   */
  public int execute(FileSystemElement element, SizeCalculationVisitor visitor) {
    var safeElement = Objects.requireNonNull(element, "element must not be null");
    var safeVisitor = Objects.requireNonNull(visitor, "visitor must not be null");

    // データ構造へ訪問者を受け入れる
    safeElement.accept(safeVisitor);

    return safeVisitor.getTotalSize();
  }
}
