package jp.ne.yonem.restful.infrastructure.otp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpVerifyForm {
  private String otp;
}
