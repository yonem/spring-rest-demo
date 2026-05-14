package jp.ne.yonem.restful.infrastructure.lesson.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @InjectMocks private AuthService sut;

  @Mock private UserAuthenticator authenticator;

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: 認証器がtrueを返す場合、サービスもtrueを返すこと")
    void test01() {
      when(authenticator.authenticate("user", "pass")).thenReturn(true);

      var result = sut.execute(authenticator, "user", "pass");

      assertThat(result).isTrue();
      verify(authenticator, times(1)).authenticate("user", "pass");
    }

    @Test
    @DisplayName("正常系: 認証器がfalseを返す場合、サービスもfalseを返すこと")
    void test02() {
      when(authenticator.authenticate(anyString(), anyString())).thenReturn(false);

      var result = sut.execute(authenticator, "guest", "wrong");

      assertThat(result).isFalse();
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: 認証器がnullの場合、NullPointerExceptionが発生すること")
    void test01() {
      assertThatThrownBy(() -> sut.execute(null, "user", "pass"))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("authenticator must not be null");
    }
  }
}
