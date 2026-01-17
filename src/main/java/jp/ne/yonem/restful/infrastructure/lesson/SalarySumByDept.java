package jp.ne.yonem.restful.infrastructure.lesson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SalarySumByDept {
  public record Employee(String department, int salary) {}

  public Map<String, Integer> legacy(List<Employee> employees) {
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

  public Map<String, Integer> modern(List<SalarySumByDept.Employee> employees) {
    return employees.stream()
        .collect(
            Collectors.groupingBy(
                SalarySumByDept.Employee::department,
                Collectors.summingInt(SalarySumByDept.Employee::salary)));
  }
}
