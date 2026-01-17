package jp.ne.yonem.restful.infrastructure.lesson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalarySumByDept {
  public record Employee(String department, int salary) {}

  public Map<String, Integer> getSalarySumByDept(List<Employee> employees) {
    Map<String, Integer> result = new HashMap<>();
    for (Employee e : employees) {
      String dept = e.department();
      int salary = e.salary();

      if (!result.containsKey(dept)) {
        result.put(dept, 0);
      }
      result.put(dept, result.get(dept) + salary);
    }
    return result;
  }
}
