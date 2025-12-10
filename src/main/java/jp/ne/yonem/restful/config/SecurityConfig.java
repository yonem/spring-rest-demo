package jp.ne.yonem.restful.config;

import java.util.Arrays;
import jp.ne.yonem.restful.infrastructure.auth.LoginUserDetailsService;
import jp.ne.yonem.restful.infrastructure.idp.JwtAuthenticationFilter;
import jp.ne.yonem.restful.infrastructure.idp.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final LoginUserDetailsService userDetailsService;
  private final JwtTokenProvider jwtTokenProvider;

  private static final String[] PERMIT_WHITELIST = {
    "/api/greeting/**", "/api/auth/**", "/api/kafka", "/api/free/**", "/api/download/**"
  };

  private static final String[] SWAGGER_WHITELIST = {
    "/v3/api-docs/**", "/swagger**/**", "/swagger-ui.html", "/webjars/**"
  };

  @Value("${encryption.password}")
  private String password;

  @Value("${api.cors.allowed-origins:}")
  private String allowedOriginsConfig;

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
  public EncryptionProvider encryptionUtil() {
    return new EncryptionProvider(password);
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) {
    var authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return new ProviderManager(authProvider);
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilterBean() {
    return new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService);
  }

  @Bean
  @Order(1)
  public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
    http.securityMatcher("/api/**")
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth -> auth.requestMatchers(PERMIT_WHITELIST).permitAll().anyRequest().permitAll())
        .addFilterBefore(jwtAuthenticationFilterBean(), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  @Order(2)
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.securityMatcher(AntPathRequestMatcher.antMatcher("/**"))
        .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers("/login")
                    .permitAll()
                    .requestMatchers(SWAGGER_WHITELIST)
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
        .logout(LogoutConfigurer::permitAll);
    return http.build();
  }

  /** CORS設定の本体を定義するメソッド */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    var configuration = new CorsConfiguration();

    // 読み込んだカンマ区切りの文字列をリストに変換
    var origins = Arrays.asList(allowedOriginsConfig.split(","));

    // ★ 許可オリジンリストをセット (マルチオリジン対応)
    configuration.setAllowedOrigins(origins);

    // ★ 許可メソッドをセット (POST/PUT/DELETE/OPTIONSなど、必要なもの全て)
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

    // ★ 許可ヘッダーをセット (カスタムヘッダーやContent-Typeなど)
    configuration.setAllowedHeaders(
        Arrays.asList("Content-Type", "Authorization", "X-Custom-Header"));

    // ★ 認証情報 (クッキー、Authorizationヘッダーなど) の送信を許可
    //   ※ allowedOrigins にワイルドカード(*)は使えなくなります
    configuration.setAllowCredentials(true);

    // ★ Preflightリクエストのキャッシュ時間を設定 (負荷軽減のため推奨)
    //   例: 1時間はOPTIONSリクエストを再送しない
    configuration.setMaxAge(3600L);

    // 全てのパス ("/**") に対して設定を適用
    var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
