package jp.ne.yonem.restful.otp;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.jboss.aerogear.security.otp.Totp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OtpServiceTest {
  private OtpService otpService;

  @BeforeEach
  void setUp() {
    otpService = new OtpService();
  }

  @Test
  void testGenerateSecretKey() {
    var secretKey = otpService.generateSecretKey();

    System.out.println("Generated Secret Key: " + secretKey);

    assertNotNull(secretKey);
    assertFalse(secretKey.isEmpty());
    assertTrue(secretKey.length() >= 26);
  }

  @Test
  void testGetGoogleAuthenticatorBarCodeUrl() {
    var secretKey = "JBSWY3DPEHPK3PXP";
    var accountName = "testuser@example.com";
    var issuer = "MyTestApp";

    var barCodeUrl = otpService.getGoogleAuthenticatorBarCodeUrl(secretKey, accountName, issuer);
    System.out.println("Generated BarCode URL: " + barCodeUrl);

    assertNotNull(barCodeUrl);
    assertTrue(barCodeUrl.startsWith("otpauth://totp/"));
    assertTrue(barCodeUrl.contains("secret=" + secretKey));
    assertTrue(barCodeUrl.contains("issuer=" + issuer));
    assertTrue(
        barCodeUrl.contains(
            URLEncoder.encode(issuer + ":" + accountName, StandardCharsets.UTF_8)
                .replace("+", "%20")));
  }

  @Test
  void testGenerateQrCodeImage() {
    var secretKey = otpService.generateSecretKey();
    var accountName = "qrtest@example.com";
    var issuer = "MyQrApp";
    var barCodeUrl = otpService.getGoogleAuthenticatorBarCodeUrl(secretKey, accountName, issuer);
    var qrCodeImageBase64 = otpService.generateQrCodeImage(barCodeUrl, 150, 150);

    System.out.println(
        "Generated QR Code Image (Base64): " + qrCodeImageBase64.substring(0, 50) + "...");

    assertNotNull(qrCodeImageBase64);
    assertTrue(qrCodeImageBase64.startsWith("iVBORw0KGgoAAA"));
  }

  @Test
  void testValidateOtp_Success() {
    var secretKey = otpService.generateSecretKey();
    var totp = new Totp(secretKey);
    var currentOtp = totp.now();

    System.out.println("Secret Key for validation: " + secretKey);
    System.out.println("Generated Current OTP for validation: " + currentOtp);

    assertTrue(otpService.validateOtp(secretKey, currentOtp));
  }

  @Test
  void testValidateOtp_Failure_WrongOtp() {
    var secretKey = otpService.generateSecretKey();
    var wrongOtp = "123456";

    System.out.println("Secret Key for failure validation: " + secretKey);
    System.out.println("Attempting to validate with wrong OTP: " + wrongOtp);

    assertFalse(otpService.validateOtp(secretKey, wrongOtp));
  }

  @Test
  void testGenerateCurrentOtp() {
    var secretKey = otpService.generateSecretKey();
    var generatedOtp = otpService.generateCurrentOtp(secretKey);

    System.out.println("Generated OTP for current time: " + generatedOtp);

    assertNotNull(generatedOtp);
    assertEquals(6, generatedOtp.length());
    assertTrue(generatedOtp.matches("\\d{6}"));
  }
}
