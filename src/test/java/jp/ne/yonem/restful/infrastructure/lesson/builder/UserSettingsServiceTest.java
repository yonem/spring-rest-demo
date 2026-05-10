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
    @DisplayName("正常系: 管理者IDの場合、ダークテーマかつ通知オフで構築されること")
    void test01() {
      var result = sut.execute("ADM_01");

      assertThat(result.theme()).isEqualTo("DARK");
      assertThat(result.notificationsEnabled()).isFalse();
    }

    @Test
    @DisplayName("正常系: ゲストIDの場合、クラシックテーマで構築されること")
    void test02() {
      var result = sut.execute("GUEST_99");

      assertThat(result.theme()).isEqualTo("CLASSIC");
    }

    @Test
    @DisplayName("正常系: 一般ユーザーの場合、デフォルト設定が維持されること")
    void test03() {
      var result = sut.execute("USER_123");

      assertThat(result.theme()).isEqualTo("LIGHT");
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: ユーザーIDがnullの場合、例外が発生すること")
    void test01() {
      assertThatThrownBy(() -> sut.execute(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("Input userId must not be null");
    }
  }
}
