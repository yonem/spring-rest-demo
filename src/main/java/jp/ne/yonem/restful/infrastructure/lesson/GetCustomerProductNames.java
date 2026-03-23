package jp.ne.yonem.restful.infrastructure.lesson;

import java.util.*;
import java.util.stream.Collectors;

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

  public Map<String, List<String>> modern(List<Order> orders) {
    return Optional.ofNullable(orders).orElse(List.of()).stream()
        .collect(
            Collectors.groupingBy(
                Order::customerName, // 1. 顧客名でグループ化
                Collectors.flatMapping(
                    o -> o.items().stream(), // 2. 注文内のアイテムを平坦化
                    Collectors.mapping(Item::name, Collectors.toList()) // 3. アイテム名を取り出してリスト化
                    )));
  }
}
