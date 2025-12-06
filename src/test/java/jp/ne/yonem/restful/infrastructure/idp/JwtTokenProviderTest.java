package jp.ne.yonem.restful.infrastructure.idp;

import static org.junit.jupiter.api.Assertions.*;

import io.jsonwebtoken.Jwts;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtTokenProviderTest {

  @Test
  @DisplayName("キーペア作成")
  void test01() throws NoSuchAlgorithmException, InvalidKeySpecException {
    var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);
    var keyPair = keyPairGenerator.genKeyPair();

    var publicKey = keyPair.getPublic();
    var privateKey = keyPair.getPrivate();

    var publicKeyPEM = encodePublicKeyToPEM(publicKey);
    var privateKeyPEM = encodePrivateKeyToPEM(privateKey);
    System.out.printf("jwt.publicKey=%s%n", publicKeyPEM);
    System.out.printf("jwt.privateKey=%s%n", privateKeyPEM);

    var loadedPublicKey = decodePublicKeyFromPEM(publicKeyPEM);
    var loadedPrivateKey = decodePrivateKeyFromPEM(privateKeyPEM);
    assertNotNull(loadedPublicKey);
    assertNotNull(loadedPrivateKey);

    assertEquals(
        Base64.getEncoder().encodeToString(loadedPublicKey.getEncoded()),
        Base64.getEncoder().encodeToString(publicKey.getEncoded()),
        "Loaded Public Key DOES NOT match original!");

    assertEquals(
        Base64.getEncoder().encodeToString(loadedPrivateKey.getEncoded()),
        Base64.getEncoder().encodeToString(privateKey.getEncoded()),
        "Loaded Private Key DOES NOT match original!");
  }

  @Test
  @DisplayName("キーペア検証")
  void test02() throws NoSuchAlgorithmException, InvalidKeySpecException {
    var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);
    var keyPair = keyPairGenerator.genKeyPair();

    var publicKey = keyPair.getPublic();
    var privateKey = keyPair.getPrivate();

    var publicPem = encodePublicKeyToPEM(publicKey);
    var privatePem = encodePrivateKeyToPEM(privateKey);
    var subject = "subject";
    var jwtTokenProvider = new JwtTokenProvider(privatePem, publicPem);
    var jwt = Jwts.builder().subject(subject).signWith(privateKey).compact();
    var claims = jwtTokenProvider.getLoginIdFromJWT(jwt);

    assertEquals(subject, claims);
    assertTrue(jwtTokenProvider.validateToken(jwt));
  }

  private static String encodePublicKeyToPEM(PublicKey publicKey) {
    return Base64.getEncoder().encodeToString(publicKey.getEncoded());
  }

  private static String encodePrivateKeyToPEM(PrivateKey privateKey) {
    return Base64.getEncoder().encodeToString(privateKey.getEncoded());
  }

  private static PublicKey decodePublicKeyFromPEM(String pemString)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    var decodedBytes = Base64.getDecoder().decode(pemString);
    var keySpec = new X509EncodedKeySpec(decodedBytes);
    var keyFactory = KeyFactory.getInstance("RSA");
    return keyFactory.generatePublic(keySpec);
  }

  private static PrivateKey decodePrivateKeyFromPEM(String pemString)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    var decodedBytes = Base64.getDecoder().decode(pemString);
    var keySpec = new PKCS8EncodedKeySpec(decodedBytes);
    var keyFactory = KeyFactory.getInstance("RSA");
    return keyFactory.generatePrivate(keySpec);
  }
}
