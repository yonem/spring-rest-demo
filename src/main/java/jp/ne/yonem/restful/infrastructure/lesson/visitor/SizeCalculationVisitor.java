package jp.ne.yonem.restful.infrastructure.lesson.visitor;

import java.util.Objects;
import lombok.Getter;

/** ファイルシステムの総容量を集計する具体的な訪問者クラスです。 */
@Getter
public class SizeCalculationVisitor implements FileSystemVisitor {

  private int totalSize = 0;

  @Override
  public void visit(VisitorFile file) {
    var safeFile = Objects.requireNonNull(file, "file must not be null");
    this.totalSize += safeFile.size();
  }

  @Override
  public void visit(VisitorFolder folder) {
    // フォルダ自体にサイズはないため何もしない（構造の巡回はFolderクラス側で行う）
    Objects.requireNonNull(folder, "folder must not be null");
  }
}
