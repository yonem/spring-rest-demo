package jp.ne.yonem.restful.demo.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
  @NotBlank(message = "ユーザー名は必須です")
  private String username;

  @Min(value = 18, message = "年齢は18歳以上である必要があります")
  private int age;
}
