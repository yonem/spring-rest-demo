package jp.ne.yonem.restful.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jp.ne.yonem.restful.validation.PasswordPolicyCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@PasswordPolicyCheck
public class PasswordForm {
  @NotNull private int policyId;
  @NotBlank private String password;
}
