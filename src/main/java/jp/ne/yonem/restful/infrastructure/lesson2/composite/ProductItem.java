package jp.ne.yonem.restful.infrastructure.lesson2.composite;

import java.util.Objects;

/** 葉（Leaf）: 個別の商品を物理的に表すクラスです。 */
public record ProductItem(String name, int price) implements CatalogComponent {

  public ProductItem(String name, int price) {
    this.name = Objects.requireNonNull(name, "name must not be null");

    if (price < 0) {
      throw new IllegalArgumentException("price must not be negative");
    }
    this.price = price;
  }
}
