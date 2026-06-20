package jp.ne.yonem.restful.infrastructure.lesson.composite;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** フォルダを表す具体的なクラス（容器要素）です。 */
public final class CompositeFolder implements FileSystemComponent {

  private final String name;
  private final List<FileSystemComponent> components = new ArrayList<>();

  public CompositeFolder(String name) {
    this.name = Objects.requireNonNull(name, "name must not be null");
  }

  /**
   * フォルダの中に要素（ファイルまたはフォルダ）を追加します。
   *
   * @param component 追加する要素
   */
  public void add(FileSystemComponent component) {
    var safeComponent = Objects.requireNonNull(component, "component must not be null");
    this.components.add(safeComponent);
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public int getSize() {
    // 自身の配下にあるすべての要素のサイズを再帰的に集計する
    return this.components.stream().mapToInt(FileSystemComponent::getSize).sum();
  }

  @Override
  public void print(String indent) {
    var safeIndent = Objects.requireNonNull(indent, "indent must not be null");
    System.out.println(safeIndent + "+ " + this.name + "/");

    // 子要素に対して再帰的に処理を呼び出す
    this.components.forEach(component -> component.print(safeIndent + "  "));
  }
}
