package jp.ne.yonem.restful.idp;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

  // SSO連携用のトークン検証エンドポイント (IDP側がSPにリダイレクトする際にJWTを渡す想定)
  // このエンドポイントは、実際にはIDPからのリダイレクト時にJWTを検証し、SP側でセッションを確立するために使用されます。
  // 今回は同一システム内なので、直接JWTを使ってリソースにアクセスできますが、概念的に記述します。
  @PostMapping("/verify-sso-token")
  public ResponseEntity<?> verifySsoToken(@RequestBody LoginResponse ssoToken) {
    if (tokenProvider.validateToken(ssoToken.getAccessToken())) {
      // トークンが有効な場合、SP側でユーザー情報を取得し、セッションを確立する処理を記述
      // 例えば、ユーザー情報をセッションに格納するなど
      var username = tokenProvider.getLoginIdFromJWT(ssoToken.getAccessToken());
      // ここで、SP側での認証済みセッション確立処理を行う
      // (例: Spring SecurityのSecurityContextに認証情報をセットするなど。ただしステートレスなJWTなのでセッションに保持は不要な場合が多い)
      return ResponseEntity.ok("SSO token verified for user: " + username);
    } else {
      return ResponseEntity.badRequest().body("Invalid SSO token");
    }
  }
}
