package jp.ne.yonem.restful.infrastructure.persistence.record;

import java.util.Objects;

public record PasswordPolicy(int id, int min, int max, String kinds, int comb) {

  /** 指定されたパスワードがポリシーに適合するか判定する */
  public boolean validate(String password) {
    if (Objects.isNull(password)) return false;

    // 長さチェック
    if (password.length() < min || max < password.length()) {
      return false;
    }

    // 使用されている文字種（kinds）のカウント
    var count = 0;
    if (kinds.contains("l") && password.matches(".*[a-z].*")) count++;
    if (kinds.contains("u") && password.matches(".*[A-Z].*")) count++;
    if (kinds.contains("d") && password.matches(".*[0-9].*")) count++;
    if (kinds.contains("s") && password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*"))
      count++;

    // 組み合わせ種類のチェック
    if (count < comb) return false;

    // 許可されていない文字が含まれていないかチェック
    var allowedChars = "";
    if (kinds.contains("l")) allowedChars += "a-z";
    if (kinds.contains("u")) allowedChars += "A-Z";
    if (kinds.contains("d")) allowedChars += "0-9";
    if (kinds.contains("s")) allowedChars += "!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?";

    var disallowedCharPattern = "[^" + allowedChars + "]+";
    return !password.matches(".*" + disallowedCharPattern + ".*");
  }
}
