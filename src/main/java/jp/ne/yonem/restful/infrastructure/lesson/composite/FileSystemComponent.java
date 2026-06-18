package jp.ne.yonem.restful.infrastructure.lesson.composite;

/** ファイルシステム要素の共通インターフェースです。 */
public interface FileSystemComponent {
  String getName();

  int getSize();

  void print(String indent);
}
