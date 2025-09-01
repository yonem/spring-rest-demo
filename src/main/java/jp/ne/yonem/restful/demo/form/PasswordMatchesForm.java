package jp.ne.yonem.restful.demo.form;

import jp.ne.yonem.restful.demo.validation.PasswordMatchesCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatchesCheck
public class PasswordMatchesForm {
  private String password;
  private String rePassword;
}
