package jp.ne.yonem.restful.idp;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  @Value("${jwt.expiration}")
  private long jwtExpirationInMs;

  private final SecretKey key;

  public JwtTokenProvider(@Value("${jwt.secret}") String jwtSecret) {
    this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
  }

  public String generateToken(Authentication authentication) {
    var userDetails = (UserDetails) authentication.getPrincipal();
    var now = new Date();
    var expiryDate = new Date(now.getTime() + jwtExpirationInMs);
    var roles =
        userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

    return Jwts.builder()
        .subject(userDetails.getUsername())
        .claim("roles", roles)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(key)
        .compact();
  }

  public String getLoginIdFromJWT(String token) {
    var claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    return claims.getSubject();
  }

  public boolean validateToken(String authToken) {
    try {
      Jwts.parser().verifyWith(key).build().parseSignedClaims(authToken);
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
}
