package jp.ne.yonem.restful.infrastructure.persistence.record;

import java.util.Objects;
import java.util.stream.Collectors;

public record PasswordPolicy(int id, int min, int max, String kinds, int comb) {

  public boolean validate(String password) {
    if (Objects.isNull(password)) return false;

    // 長さチェック
    var length = password.length();
    if (length < min || max < length) return false;

    // 有効な文字種のリストを取得
    var activeKinds = PasswordCharKind.from(kinds);

    // 1. 組み合わせ種類のチェック (Stream APIで宣言的に記述)
    var combinationCount = activeKinds.stream().filter(kind -> kind.matches(password)).count();
    if (combinationCount < comb) return false;

    // 2. 許可されていない文字のチェック
    var allowedPattern =
        activeKinds.stream().map(PasswordCharKind::getAllowedPattern).collect(Collectors.joining());

    var disallowedCharRegex = ".*[^" + allowedPattern + "].*";
    return !password.matches(disallowedCharRegex);
  }
}
