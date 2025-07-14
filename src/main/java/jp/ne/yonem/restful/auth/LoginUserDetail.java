package jp.ne.yonem.restful.auth;

import java.util.Collection;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class LoginUserDetail extends User {
  private final Integer id;
  private final String email;

  public LoginUserDetail(
      Integer id,
      String username,
      String password,
      String email,
      Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);
    this.id = id;
    this.email = email;
  }
}
