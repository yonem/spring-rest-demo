package jp.ne.yonem.restful.infrastructure.lesson.strategy;

public class DiscountService {

  public int legacy(String rank, int price) {
    if ("GOLD".equals(rank)) {
      return (int) (price * 0.8);
    } else if ("SILVER".equals(rank)) {
      return (int) (price * 0.9);
    } else {
      return price;
    }
  }
}
