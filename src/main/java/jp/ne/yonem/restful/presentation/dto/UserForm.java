package jp.ne.yonem.restful.presentation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserForm {
  @NotBlank(message = "ユーザー名は必須です")
  private String username;

  @Min(value = 18, message = "年齢は18歳以上である必要があります")
  private int age;
}
