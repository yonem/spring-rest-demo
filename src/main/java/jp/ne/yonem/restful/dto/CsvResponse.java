package jp.ne.yonem.restful.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CsvResponse {
  private String header;
  private List<String> body;
}
