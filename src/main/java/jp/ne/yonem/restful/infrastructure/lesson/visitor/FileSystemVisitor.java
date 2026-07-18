package jp.ne.yonem.restful.infrastructure.lesson.visitor;

/** データ構造を訪問して処理を実行する訪問者インターフェースです。 */
public interface FileSystemVisitor {
  void visit(VisitorFile file);

  void visit(VisitorFolder folder);
}
