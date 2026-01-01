package jp.ne.yonem.restful.infrastructure.persistence.record;

import java.util.Objects;
import java.util.stream.Collectors;

public record PasswordPolicy(int id, int min, int max, String kinds, int comb) {

  /**
   * 指定されたパスワードがこのポリシーの制約を満たしているか検証します。
   *
   * <p>以下のチェックを順番に行います：
   *
   * <ul>
   *   <li>長さチェック: {@code min}文字以上、{@code max}文字以下であること
   *   <li>組み合わせチェック: 有効な文字種（{@code kinds}）のうち、最低{@code comb}種類以上が含まれていること
   *   <li>許可文字チェック: 有効な文字種として定義されていない文字が含まれていないこと
   * </ul>
   *
   * @param password 検証対象のパスワード。{@code null} の場合は {@code false} を返します
   * @return ポリシーに適合する場合は {@code true}、適合しない場合は {@code false}
   */
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
