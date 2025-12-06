package jp.ne.yonem.restful.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountForm extends PasswordForm {
  @NotBlank
  @Length(min = 3, max = 10, message = "E002")
  private String name;

  public AccountForm(int policyId, String password, String name) {
    super(policyId, password);
    this.name = name;
  }
}
