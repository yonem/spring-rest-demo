package jp.ne.yonem.restful.service;

import jakarta.validation.Validator;
import jp.ne.yonem.restful.form.ValidSampleForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidSampleService {
  private final Validator validator;

  public void execute(ValidSampleForm form) {
    var errors = validator.validate(form);

    if (!errors.isEmpty()) {
      for (var err : errors) {
        System.err.println(err.getPropertyPath() + ": " + err.getMessage());
      }
      throw new IllegalArgumentException("バリデーションエラーが発生しました");
    }
  }
}
