package jp.ne.yonem.restful.otp;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import org.apache.commons.codec.binary.Base32;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.stereotype.Service;

@Service
public class OtpService {
  private static final int SECRET_KEY_LENGTH = 16;

  /**
   * 新しいシークレットキーを生成します。 Google AuthenticatorはBase32エンコードされたキーを期待します。
   *
   * @return Base32エンコードされたシークレットキー
   */
  public String generateSecretKey() {
    var random = new SecureRandom();
    var bytes = new byte[SECRET_KEY_LENGTH];
    var base32 = new Base32();
    random.nextBytes(bytes);
    return base32.encodeToString(bytes);
  }

  /**
   * Google Authenticator用のURIを生成します。
   * otpauth://totp/{issuer}:{accountName}?secret={secret}&issuer={issuer}
   *
   * @param secretKey Base32エンコードされたシークレットキー
   * @param accountName ユーザーアカウント名（例: ユーザーのメールアドレス）
   * @param issuer サービス提供者名（例: アプリケーション名）
   * @return Google Authenticator用のURI
   */
  public String getGoogleAuthenticatorBarCodeUrl(
      String secretKey, String accountName, String issuer) {
    try {
      return "otpauth://totp/"
          + URLEncoder.encode(issuer + ":" + accountName, "UTF-8").replace("+", "%20")
          + "?secret="
          + URLEncoder.encode(secretKey, "UTF-8").replace("+", "%20")
          + "&issuer="
          + URLEncoder.encode(issuer, "UTF-8").replace("+", "%20");
    } catch (UnsupportedEncodingException e) {
      throw new IllegalStateException("URL encoding not supported", e);
    }
  }

  /**
   * QRコード画像を生成し、Base64エンコードされた文字列として返します。
   *
   * @param barCodeUrl Google Authenticator用のURI
   * @param width QRコードの幅
   * @param height QRコードの高さ
   * @return Base64エンコードされたQRコード画像データ（PNG形式）
   */
  public String generateQrCodeImage(String barCodeUrl, int width, int height) {

    try (var pngOutputStream = new ByteArrayOutputStream(); ) {
      var hints = new HashMap<EncodeHintType, Object>();
      hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
      hints.put(EncodeHintType.MARGIN, 1);

      var qrCodeWriter = new QRCodeWriter();
      var bitMatrix = qrCodeWriter.encode(barCodeUrl, BarcodeFormat.QR_CODE, width, height, hints);

      MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
      var qrCodeBytes = pngOutputStream.toByteArray();

      return Base64.getEncoder().encodeToString(qrCodeBytes);
    } catch (WriterException | IOException e) {
      throw new IllegalStateException("Failed to generate QR code", e);
    }
  }

  /**
   * OTPを検証します。
   *
   * @param secretKey Base32エンコードされたシークレットキー
   * @param otp ユーザーが入力したOTP
   * @return OTPが有効な場合はtrue、そうでない場合はfalse
   */
  public boolean validateOtp(String secretKey, String otp) {
    var totp = new Totp(secretKey);
    return totp.verify(otp);
  }

  /**
   * 現在時刻に基づいてOTPを生成します。（テスト用、本番ではクライアントが生成するOTPを検証）
   *
   * @param secretKey Base32エンコードされたシークレットキー
   * @return 生成されたOTP
   */
  public String generateCurrentOtp(String secretKey) {
    var totp = new Totp(secretKey);
    return totp.now();
  }
}
