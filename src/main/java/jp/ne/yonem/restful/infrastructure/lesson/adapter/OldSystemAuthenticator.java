package jp.ne.yonem.restful.infrastructure.lesson.adapter;

import java.util.Objects;

/** 外部ライブラリや古いシステムに存在する、修正不可能なクラスです。 */
class OldSystemAuthenticator {

  // メソッド名や引数の順序が現在のインターフェースと異なります
  public int validateUser(String pass, String user) {
    return Objects.equals(user, "admin") && Objects.equals(pass, "1234") ? 1 : 0;
  }
}
