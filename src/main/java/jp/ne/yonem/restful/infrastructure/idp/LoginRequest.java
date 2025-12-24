package jp.ne.yonem.restful.infrastructure.idp;

import lombok.Data;
import lombok.ToString;

@Data
public class LoginRequest {
  private String username;
  @ToString.Exclude private String password;
}
