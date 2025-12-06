package jp.ne.yonem.restful.infrastructure.otp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpResponse {
  private String secretKey;
  private String qrCodeImageBase64;
  private String message;
}
