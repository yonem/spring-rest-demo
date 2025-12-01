package jp.ne.yonem.restful.entity;

import java.util.Date;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
  private Integer id;
  private String userName;
  private String email;
  private String password;
  private Integer roles;
  private Date createdAt;
}
