package jp.ne.yonem.restful.infrastructure.persistence.record;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Team {
  private Integer id;
  private String name;
  private List<Member> members; // チームに所属するメンバーのリスト
}
