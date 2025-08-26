package jp.ne.yonem.restful.demo.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ValidSampleForm {

  @NotNull(message = "W100100")
  private String messageId;

  @NotBlank(message = "W100101")
  private String message;
}
