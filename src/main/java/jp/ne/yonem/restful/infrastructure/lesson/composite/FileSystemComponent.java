package jp.ne.yonem.restful.infrastructure.lesson.composite;

/** ファイルシステム要素の共通インターフェースです。 */
public sealed interface FileSystemComponent permits CompositeFile, CompositeFolder {
  String getName();

  int getSize();

  void print(String indent);
}
