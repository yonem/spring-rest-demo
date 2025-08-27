package jp.ne.yonem.restful.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MessageResponse {
  private String messageId;
  private String message;
}
