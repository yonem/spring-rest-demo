package jp.ne.yonem.restful.otp;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OtpController {
  private final OtpService otpService;
  private final Map<String, String> userSecretKeys = new HashMap<>();

  /**
   * 新しいシークレットキーとQRコード画像を生成します。 ユーザーが初めてOTPを設定する際に呼び出されます。
   *
   * @param userId ユーザーID（例: ユーザーのメールアドレス）
   * @return シークレットキーとBase64エンコードされたQRコード画像のマップ
   */
  @GetMapping("/generate/{userId}")
  public ResponseEntity<OtpResponse> generateOtpConfig(@PathVariable String userId) {
    var secretKey = otpService.generateSecretKey();
    var issuer = "MyWebApp";
    userSecretKeys.put(userId, secretKey);
    var barCodeUrl = otpService.getGoogleAuthenticatorBarCodeUrl(secretKey, userId, issuer);
    var qrCodeImageBase64 = otpService.generateQrCodeImage(barCodeUrl, 200, 200);
    var response = new OtpResponse();

    response.setSecretKey(secretKey);
    response.setQrCodeImageBase64(qrCodeImageBase64);
    response.setMessage("QRコードをGoogle Authenticatorアプリでスキャンしてください。");
    return ResponseEntity.ok(response);
  }

  /**
   * ユーザーが入力したOTPを検証します。
   *
   * @param userId ユーザーID
   * @param requestBody ユーザーが入力したOTP
   * @return 検証結果
   */
  @PostMapping("/verify/{userId}")
  public ResponseEntity<Map<String, String>> verifyOtp(
      @PathVariable String userId, @RequestBody OtpVerifyForm requestBody) {
    var otp = requestBody.getOtp();

    if (Objects.isNull(otp) || otp.isEmpty()) {
      return ResponseEntity.badRequest().body(Map.of("message", "OTPを入力してください。"));
    }
    var secretKey = userSecretKeys.get(userId);

    if (Objects.isNull(secretKey)) {
      return ResponseEntity.badRequest().body(Map.of("message", "このユーザーのOTP設定が見つかりません。"));
    }
    var isValid = otpService.validateOtp(secretKey, otp);
    var response = new HashMap<String, String>();

    if (isValid) {
      response.put("message", "OTP検証成功！");
      return ResponseEntity.ok(response);
    } else {
      response.put("message", "OTP検証失敗。");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
  }
}
