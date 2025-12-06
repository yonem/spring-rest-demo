package jp.ne.yonem.restful.infrastructure.idp;

import lombok.Data;

@Data
public class LoginRequest {
  private String username;
  private String password;
}
