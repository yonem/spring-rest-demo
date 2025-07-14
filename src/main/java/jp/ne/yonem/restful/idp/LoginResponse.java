package jp.ne.yonem.restful.idp;

import lombok.Data;

@Data
public class LoginResponse {

  public LoginResponse(String accessToken) {
    this.accessToken = accessToken;
  }

  private String accessToken;
  private String tokenType = "Bearer";
}
