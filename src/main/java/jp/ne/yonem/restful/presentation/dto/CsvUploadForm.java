package jp.ne.yonem.restful.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CsvUploadForm {

  @NotBlank
  @Length(min = 3, max = 10, message = "E002")
  private String name;

  private MultipartFile file;
}
