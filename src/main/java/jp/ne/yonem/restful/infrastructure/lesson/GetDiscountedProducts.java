package jp.ne.yonem.restful.infrastructure.lesson;

import java.util.ArrayList;
import java.util.List;
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
}
