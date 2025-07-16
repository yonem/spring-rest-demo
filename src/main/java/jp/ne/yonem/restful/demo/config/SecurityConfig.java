package jp.ne.yonem.restful.demo.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jp.ne.yonem.restful.auth.LoginUserDetailsService;
import jp.ne.yonem.restful.idp.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@SecurityScheme(
    name = "Bearer Authentication", // Swagger UIで表示される認証スキームの名前
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer",
    in = SecuritySchemeIn.HEADER // ヘッダーとして渡すことを指定
    )
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
  private final LoginUserDetailsService userDetailsService;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  /**
   * パスワードエンコーダー
   *
   * @return BCryptPasswordEncoderのインスタンス
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) {
    var authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return new ProviderManager(authProvider);
  }

  //  @Bean
  //  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
  //    http.csrf(AbstractHttpConfigurer::disable) // REST APIなのでCSRFは無効
  //        .exceptionHandling(
  //            exception -> exception.authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
  //        .sessionManagement(
  //            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
  //        .authorizeHttpRequests(
  //            authorize ->
  //                authorize
  //                    .requestMatchers("/api/auth/**", "/swagger-ui/**")
  //                    .permitAll()
  //                    .anyRequest()
  //                    .permitAll());
  //
  //    // JWTフィルターをUsernamePasswordAuthenticationFilterの前に配置
  //    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
  //    return http.build();
  //  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
    return http.build();
  }
}
