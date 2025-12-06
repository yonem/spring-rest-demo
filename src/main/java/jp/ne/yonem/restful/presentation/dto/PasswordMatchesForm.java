package jp.ne.yonem.restful.presentation.dto;

import jp.ne.yonem.restful.infrastructure.validation.PasswordMatchesCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatchesCheck(message = "E004")
public class PasswordMatchesForm {
  private String password;
  private String rePassword;
}
