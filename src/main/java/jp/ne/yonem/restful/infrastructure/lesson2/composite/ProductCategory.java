package jp.ne.yonem.restful.infrastructure.lesson2.composite;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** 枝（Composite）: 複数の商品やサブカテゴリーを包含するカテゴリークラスです。 */
public class ProductCategory implements CatalogComponent {

  private final String name;
  private final List<CatalogComponent> children = new ArrayList<>();

  public ProductCategory(String name) {
    this.name = Objects.requireNonNull(name, "name must not be null");
  }

  public void add(CatalogComponent component) {
    var safeComponent = Objects.requireNonNull(component, "component must not be null");
    this.children.add(safeComponent);
  }

  @Override
  public String name() {
    return this.name;
  }

  /** カテゴリー配下の全要素の価格を再帰的に集計します。 */
  @Override
  public int price() {
    return this.children.stream().mapToInt(CatalogComponent::price).sum();
  }
}
