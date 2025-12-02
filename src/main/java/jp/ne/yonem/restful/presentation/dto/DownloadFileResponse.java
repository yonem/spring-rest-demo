package jp.ne.yonem.restful.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DownloadFileResponse {
  private HttpHeaders header;
  private byte[] file;
}
