package jp.ne.yonem.restful.infrastructure.lesson.visitor;

/** 訪問者を受け入れるデータ構造のインターフェースです。 */
public interface FileSystemElement {
  void accept(FileSystemVisitor visitor);
}
