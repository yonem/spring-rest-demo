package jp.ne.yonem.restful.infrastructure.lesson;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Data;

/** 副作用のあるループの「クリーン化」 */
public class GetDiscountedProducts {

  @Data
  public static class Product {
    Integer price;
  }

  public List<Product> legacy(List<Product> products) {
    List<Product> results = new ArrayList<>();

    for (Product p : products) {

      if (p.getPrice() > 10000) { // 1万円以上は20%オフ
        p.setPrice((int) (p.getPrice() * 0.8));
        results.add(p);

      } else if (p.getPrice() > 5000) { // 5千円以上は10%オフ
        p.setPrice((int) (p.getPrice() * 0.9));
        results.add(p);
      }
    }
    return results;
  }

  public record ProductRec(Integer price) {}

  public List<ProductRec> modern(List<ProductRec> productRecs) {
    return Optional.ofNullable(productRecs).orElse(List.of()).stream()
        .map(
            productRec -> {
              if (10_000 < productRec.price())
                return new ProductRec((int) (productRec.price() * 0.8));
              if (5_000 < productRec.price())
                return new ProductRec((int) (productRec.price() * 0.9));
              return productRec;
            })
        .toList();
  }

  public List<ProductRec> modern2(List<ProductRec> productRecs) {
    return Optional.ofNullable(productRecs).orElse(List.of()).stream()
        .map(this::applyDiscount) // 何をしているか（割引適用）が一目でわかる
        .toList();
  }

  private ProductRec applyDiscount(ProductRec productRec) {
    var currentPrice = productRec.price();

    // 価格帯に応じた「新しい価格」を算出する責務
    var discountedPrice =
        switch (currentPrice) {
          case Integer p when 10_000 < p -> (int) (p * 0.8);
          case Integer p when 5_000 < p -> (int) (p * 0.9);
          default -> currentPrice;
        };
    return new ProductRec(discountedPrice);
  }
}
