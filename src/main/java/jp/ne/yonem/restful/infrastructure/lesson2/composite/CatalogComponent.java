package jp.ne.yonem.restful.infrastructure.lesson2.composite;

/** ツリー構造を構成する要素の共通インターフェースです。 */
public interface CatalogComponent {
  String name();

  int price();
}
