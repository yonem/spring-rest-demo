package jp.ne.yonem.restful.infrastructure.lesson.composite;

import java.util.Objects;
import org.springframework.stereotype.Service;

/** コンポジットパターンによる構造を処理するサービスです。 */
@Service
public class CompositeStructureService {

  /**
   * 指定された要素の総サイズを取得します。
   *
   * @param component ファイルシステム要素
   * @return 総容量(KB)
   */
  public int calculateTotalSize(FileSystemComponent component) {
    var safeComponent = Objects.requireNonNull(component, "component must not be null");
    return safeComponent.getSize();
  }
}
