package jp.ne.yonem.restful.idp;

import io.jsonwebtoken.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;
import jp.ne.yonem.restful.auth.LoginUserDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  @Value("${jwt.expiration}")
  private long jwtExpirationInMs;

  private final PrivateKey privateKey;
  private final PublicKey publicKey;

  public JwtTokenProvider(
      @Value("${jwt.privateKey}") String privateKey, @Value("${jwt.publicKey}") String publicKey)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    this.privateKey = decodePrivateKeyFromPEM(privateKey);
    this.publicKey = decodePublicKeyFromPEM(publicKey);
  }

  public String generateToken(Authentication authentication) {
    var userDetails = (LoginUserDetail) authentication.getPrincipal();
    var now = new Date();
    var expiryDate = new Date(now.getTime() + jwtExpirationInMs);
    var roles =
        userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

    return Jwts.builder()
        .subject(userDetails.getEmail())
        .claim("roles", roles)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(privateKey)
        .compact();
  }

  public String getLoginIdFromJWT(String token) {
    var claims = Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(token).getPayload();
    return claims.getSubject();
  }

  public boolean validateToken(String authToken) {
    try {
      Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(authToken);
      return true;
    } catch (MalformedJwtException ex) {
      // "Invalid JWT token"
    } catch (ExpiredJwtException ex) {
      // "Expired JWT token"
    } catch (UnsupportedJwtException ex) {
      // "Unsupported JWT token"
    } catch (IllegalArgumentException ex) {
      // "JWT claims string is empty."
    }
    return false;
  }

  public static PublicKey decodePublicKeyFromPEM(String pemString)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    var decodedBytes = Base64.getDecoder().decode(pemString);
    var keySpec = new X509EncodedKeySpec(decodedBytes);
    var keyFactory = KeyFactory.getInstance("RSA");
    return keyFactory.generatePublic(keySpec);
  }

  public static PrivateKey decodePrivateKeyFromPEM(String pemString)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    var decodedBytes = Base64.getDecoder().decode(pemString);
    var keySpec = new PKCS8EncodedKeySpec(decodedBytes);
    var keyFactory = KeyFactory.getInstance("RSA");
    return keyFactory.generatePrivate(keySpec);
  }
}
