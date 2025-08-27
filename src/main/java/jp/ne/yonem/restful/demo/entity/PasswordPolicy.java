package jp.ne.yonem.restful.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordPolicy {
  private int id;
  private int min;
  private int max;
  private String kinds;
  private int comb;
}
