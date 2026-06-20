package jp.ne.yonem.restful.infrastructure.lesson.composite;

import java.util.Objects;

/** ファイルを表す具体的なクラス（末端要素）です。 */
public final class CompositeFile implements FileSystemComponent {

  private final String name;
  private final int size;

  public CompositeFile(String name, int size) {
    this.name = Objects.requireNonNull(name, "name must not be null");
    this.size = size;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public int getSize() {
    return this.size;
  }

  @Override
  public void print(String indent) {
    var safeIndent = Objects.requireNonNull(indent, "indent must not be null");
    System.out.println(safeIndent + "- " + this.name + " (" + this.size + "KB)");
  }
}
