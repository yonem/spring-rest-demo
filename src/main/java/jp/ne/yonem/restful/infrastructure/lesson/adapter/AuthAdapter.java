package jp.ne.yonem.restful.infrastructure.lesson.adapter;

import java.util.Objects;
import lombok.RequiredArgsConstructor;

/** 古い認証システムを現在のインターフェースに適合させるアダプターです。 */
@RequiredArgsConstructor
public class AuthAdapter implements UserAuthenticator {

  private final OldSystemAuthenticator oldSystem;

  @Override
  public boolean authenticate(String username, String password) {

    // 古いシステムの戻り値（int）を、新しい期待値（boolean）に変換して橋渡しします
    var result = oldSystem.validateUser(password, username);
    return Objects.equals(result, 1);
  }
}
