package jp.ne.yonem.restful.infrastructure.lesson.visitor;

import java.util.Objects;

/** ファイルを表すデータ構造クラスです。 */
public record VisitorFile(String name, int size) implements FileSystemElement {

  public VisitorFile(String name, int size) {
    this.name = Objects.requireNonNull(name, "name must not be null");
    this.size = size;
  }

  @Override
  public void accept(FileSystemVisitor visitor) {
    var safeVisitor = Objects.requireNonNull(visitor, "visitor must not be null");

    // 訪問者に自分自身を処理してもらう
    safeVisitor.visit(this);
  }
}
