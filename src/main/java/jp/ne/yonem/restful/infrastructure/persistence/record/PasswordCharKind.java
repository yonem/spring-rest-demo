package jp.ne.yonem.restful.infrastructure.persistence.record;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;

public enum PasswordCharKind {
  LOWERCASE('l', ".*[a-z].*", "a-z"),
  UPPERCASE('u', ".*[A-Z].*", "A-Z"),
  DIGIT('d', ".*[0-9].*", "0-9"),
  SYMBOL(
      's',
      ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*",
      "!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?");

  private final char code;
  private final String regex;
  @Getter private final String allowedPattern;

  PasswordCharKind(char code, String regex, String allowedPattern) {
    this.code = code;
    this.regex = regex;
    this.allowedPattern = allowedPattern;
  }

  public static List<PasswordCharKind> from(String kinds) {
    return Arrays.stream(values()).filter(k -> 0 <= kinds.indexOf(k.code)).toList();
  }

  public boolean matches(String password) {
    return password.matches(regex);
  }
}
