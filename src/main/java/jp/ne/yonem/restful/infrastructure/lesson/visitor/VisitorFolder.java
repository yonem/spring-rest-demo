package jp.ne.yonem.restful.infrastructure.lesson.visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;

/** フォルダを表すデータ構造クラスです。 */
public class VisitorFolder implements FileSystemElement {

  @Getter private final String name;
  private final List<FileSystemElement> children = new ArrayList<>();

  public VisitorFolder(String name) {
    this.name = Objects.requireNonNull(name, "name must not be null");
  }

  public void add(FileSystemElement element) {
    var safeElement = Objects.requireNonNull(element, "element must not be null");
    this.children.add(safeElement);
  }

  @Override
  public void accept(FileSystemVisitor visitor) {
    var safeVisitor = Objects.requireNonNull(visitor, "visitor must not be null");

    // フォルダ自身の処理を行い、配下の子要素にも訪問者を巡回させる
    safeVisitor.visit(this);
    this.children.forEach(child -> child.accept(safeVisitor));
  }
}
