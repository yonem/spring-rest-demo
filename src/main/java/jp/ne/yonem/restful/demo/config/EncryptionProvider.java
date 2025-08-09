package jp.ne.yonem.restful.demo.config;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;

@Component
public class EncryptionProvider {
  private final Key secretKey;
  private static final String ALGORITHM = "AES";

  public EncryptionProvider(String password) {
    var passwordBytes = password.getBytes(StandardCharsets.UTF_8);
    var keyBytes = new byte[16];
    System.arraycopy(
        passwordBytes, 0, keyBytes, 0, Math.min(passwordBytes.length, keyBytes.length));
    this.secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
  }

  public String encrypt(String data) throws Exception {
    var cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    var encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
    return Base64.getEncoder().encodeToString(encryptedBytes);
  }

  public String decrypt(String encryptedData) throws Exception {
    var cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, secretKey);
    var decodedBytes = Base64.getDecoder().decode(encryptedData);
    var decryptedBytes = cipher.doFinal(decodedBytes);
    return new String(decryptedBytes, StandardCharsets.UTF_8);
  }
}
