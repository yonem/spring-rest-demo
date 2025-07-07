package jp.ne.yonem.restful.demo.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  /**
   * HTTPリクエストに対するセキュリティ設定
   *
   * @param http HttpSecurityオブジェクト
   * @return 構築されたSecurityFilterChain
   * @throws Exception 設定エラー
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            authorize ->
                authorize
                    // 認証不要でアクセスできるパスを設定
                    .requestMatchers("/**") // NOTE: 開発時は認証不要にする
                    .permitAll()
                    .requestMatchers("/public/**", "/images/**", "/css/**", "/js/**")
                    .permitAll()
                    // /admin/** は ADMIN ロールが必要
                    .requestMatchers("/admin/**")
                    .hasRole("ADMIN")
                    // /user/** は USER ロールが必要
                    .requestMatchers("/user/**")
                    .hasRole("USER")
                    // その他の全てのリクエストは認証が必要
                    .anyRequest()
                    .authenticated())
        // フォームベース認証を有効にする
        .formLogin(
            formLogin ->
                formLogin
                    .loginPage("/login") // ログインページのURLを指定
                    .defaultSuccessUrl("/home", true) // ログイン成功後のリダイレクト先
                    .permitAll() // ログインページとログイン処理は認証不要
            )
        // ログアウト処理を有効にする
        .logout(
            logout ->
                logout
                    .logoutUrl("/logout") // ログアウト処理のURL
                    .logoutSuccessUrl("/login?logout") // ログアウト成功後のリダイレクト先
                    .permitAll() // ログアウト処理は認証不要
            )
        // CSRF保護を有効にする（デフォルトで有効）
        .csrf(withDefaults());

    return http.build();
  }

  /**
   * パスワードエンコーダー
   *
   * @return BCryptPasswordEncoderのインスタンス
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * インメモリユーザー
   *
   * @param passwordEncoder パスワードエンコーダー
   * @return UserDetailsServiceのインスタンス
   */
  @Bean
  public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    // ADMIN ロールを持つユーザー
    var admin =
        User.builder()
            .username("admin")
            .password(passwordEncoder.encode("adminpass")) // パスワードをエンコード
            .roles("ADMIN", "USER") // 複数のロールを付与
            .build();

    // USER ロールを持つユーザー
    var user =
        User.builder()
            .username("user")
            .password(passwordEncoder.encode("userpass")) // パスワードをエンコード
            .roles("USER")
            .build();

    return new InMemoryUserDetailsManager(admin, user);
  }
}
