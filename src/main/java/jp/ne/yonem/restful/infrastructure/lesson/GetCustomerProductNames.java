package jp.ne.yonem.restful.infrastructure.lesson;

import java.util.*;

/** 複雑な集計（Grouping & Mapping） */
public class GetCustomerProductNames {

  public record Item(String name) {}

  public record Order(String customerName, List<Item> items) {}

  public Map<String, List<String>> legacy(List<Order> orders) {
    Map<String, List<String>> result = new HashMap<>();

    for (Order order : orders) {
      String customerName = order.customerName();
      if (!result.containsKey(customerName)) {
        result.put(customerName, new ArrayList<>());
      }

      for (Item item : order.items()) {
        result.get(customerName).add(item.name());
      }
    }
    return result;
  }
}
