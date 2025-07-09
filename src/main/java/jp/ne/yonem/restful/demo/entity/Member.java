package jp.ne.yonem.restful.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
  private Integer id;
  private Integer teamId; // 外部キー
  private String name;
}
