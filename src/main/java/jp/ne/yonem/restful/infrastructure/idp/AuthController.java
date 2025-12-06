package jp.ne.yonem.restful.infrastructure.idp;

import java.util.Objects;
import jp.ne.yonem.restful.infrastructure.auth.LoginUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider tokenProvider;

  @PostMapping("/userprofile")
  public ResponseEntity<?> userprofile(@AuthenticationPrincipal LoginUserDetail loginUser) {

    if (Objects.nonNull(loginUser)) {
      return ResponseEntity.ok(loginUser);
    } else {
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
    var authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    var jwt = tokenProvider.generateToken(authentication);
    return ResponseEntity.ok(new LoginResponse(jwt));
  }

  @PostMapping("/verify-sso-token")
  public ResponseEntity<?> verifySsoToken(@RequestBody LoginResponse ssoToken) {

    if (tokenProvider.validateToken(ssoToken.getAccessToken())) {
      var username = tokenProvider.getLoginIdFromJWT(ssoToken.getAccessToken());
      return ResponseEntity.ok("SSO token verified for user: " + username);

    } else {
      return ResponseEntity.badRequest().body("Invalid SSO token");
    }
  }
}
