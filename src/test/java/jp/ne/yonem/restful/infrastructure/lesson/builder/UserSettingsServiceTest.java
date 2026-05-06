package jp.ne.yonem.restful.infrastructure.lesson.builder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserSettingsServiceTest {
  @InjectMocks private UserSettingsService sut;

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: 一般ユーザーの場合、デフォルト設定で構築されること")
    void test01() {
      var result = sut.execute("USR001");

      assertThat(result.userId()).isEqualTo("USR001");
      assertThat(result.theme()).isEqualTo("LIGHT");
      assertThat(result.notificationsEnabled()).isTrue();
    }

    @Test
    @DisplayName("正常系: 管理者ユーザーの場合、ダークテーマかつ通知オフで構築されること")
    void test02() {
      var result = sut.execute("ADM999");

      assertThat(result.theme()).isEqualTo("DARK");
      assertThat(result.notificationsEnabled()).isFalse();
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: ユーザーIDがnullの場合、NullPointerExceptionが発生すること")
    void test01() {
      assertThatThrownBy(() -> sut.execute(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("userIdは必須です");
    }
  }
}
