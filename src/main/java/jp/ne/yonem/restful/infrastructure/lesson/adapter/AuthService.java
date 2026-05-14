package jp.ne.yonem.restful.infrastructure.lesson.adapter;

import java.util.Objects;
import org.springframework.stereotype.Service;

/** 認証処理を統括するサービスです。 */
@Service
public class AuthService {

  /**
   * 指定された認証器を使用してユーザーを認証します。
   *
   * @param authenticator 使用する認証インターフェース
   * @param username ユーザー名
   * @param password パスワード
   * @return 認証成功時はtrue
   */
  public boolean execute(UserAuthenticator authenticator, String username, String password) {
    var safeAuth = Objects.requireNonNull(authenticator, "authenticator must not be null");
    var safeUser = Objects.requireNonNull(username, "username must not be null");
    var safePass = Objects.requireNonNull(password, "password must not be null");

    return safeAuth.authenticate(safeUser, safePass);
  }
}
