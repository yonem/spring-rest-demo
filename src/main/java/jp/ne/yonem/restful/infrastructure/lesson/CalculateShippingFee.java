package jp.ne.yonem.restful.infrastructure.lesson;


/** 複雑なビジネスロジックの「カプセル化」 */
public class CalculateShippingFee {

  public int legacy(String method, int weight) {
    if ("STANDARD".equals(method)) {
      return weight * 100;
    } else if ("EXPRESS".equals(method)) {
      return weight * 200 + 500;
    } else if ("INTERNATIONAL".equals(method)) {
      if (weight > 10) {
        return weight * 500 + 2000;
      } else {
        return weight * 500 + 1000;
      }
    } else {
      throw new IllegalArgumentException("Unknown method");
    }
  }
}
