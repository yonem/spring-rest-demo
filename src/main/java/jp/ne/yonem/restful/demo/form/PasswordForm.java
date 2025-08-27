package jp.ne.yonem.restful.demo.form;

import jakarta.validation.constraints.NotNull;
import jp.ne.yonem.restful.demo.validation.PasswordPolicyCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@PasswordPolicyCheck
public class PasswordForm {
  @NotNull private int policyId;
  @NotNull private String password;

  @Length(min = 3, max = 10, message = "E002")
  private String name;
}
