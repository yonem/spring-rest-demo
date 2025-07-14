package jp.ne.yonem.restful.auth;

import java.util.List;
import jp.ne.yonem.restful.demo.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginUserDetailsService implements UserDetailsService {
  private final UserMapper mapper;

  @Override
  public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
    var user = mapper.findByEmail(loginName);
    return new LoginUserDetail(
        user.getId(),
        user.getUserName(),
        user.getPassword(),
        user.getEmail(),
        List.of(new SimpleGrantedAuthority("USER")));
  }
}
