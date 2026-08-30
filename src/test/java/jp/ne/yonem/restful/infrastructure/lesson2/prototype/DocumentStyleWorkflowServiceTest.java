package jp.ne.yonem.restful.infrastructure.lesson2.prototype;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DocumentStyleWorkflowServiceTest {

  @InjectMocks private DocumentStyleWorkflowService sut;

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: プロトタイプから複製され、元のプロトタイプに影響を与えずカスタマイズできること")
    void test01() {
      var registry = new DocumentStyleRegistry();

      var result = sut.execute(registry, "DEFAULT", 18);

      assertThat(result).isEqualTo("Style[DEFAULT] Font:Arial Size:18 Color:#000000");

      // レジストリ内の元のプロトタイプ（Size 12）に影響を与えていないことを確認
      var originalStyle = registry.getCloned("DEFAULT");
      assertThat(originalStyle.getFontSize()).isEqualTo(12);
    }

    @Test
    @DisplayName("正常系: 異なるプロトタイプキー（DARK_MODE）から複製して実行できること")
    void test02() {
      var registry = new DocumentStyleRegistry();

      var result = sut.execute(registry, "DARK_MODE", 20);

      assertThat(result).isEqualTo("Style[DARK_MODE] Font:Consolas Size:20 Color:#FFFFFF");
    }

    @Test
    @DisplayName("正常系: Prototype自体のcloneメソッドが完全に異なる参照（別インスタンス）を返すこと")
    void test03() {
      var prototype = new DocumentStylePrototype("Courier", 10, "#111111");
      var cloned = prototype.clone();

      assertThat(cloned).isNotSameAs(prototype);
      assertThat(cloned.getFontName()).isEqualTo(prototype.getFontName());
      assertThat(cloned.getFontSize()).isEqualTo(prototype.getFontSize());
      assertThat(cloned.getThemeColor()).isEqualTo(prototype.getThemeColor());
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: レジストリがnullの場合、NullPointerExceptionが発生すること")
    void test01() {
      assertThatThrownBy(() -> sut.execute(null, "DEFAULT", 16))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("registry must not be null");
    }

    @Test
    @DisplayName("異常系: スタイルキーがnullの場合、NullPointerExceptionが発生すること")
    void test02() {
      var registry = new DocumentStyleRegistry();
      assertThatThrownBy(() -> sut.execute(registry, null, 16))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("styleKey must not be null");
    }

    @Test
    @DisplayName("異常系: 存在しないスタイルキーを指定した場合、IllegalArgumentExceptionが発生すること")
    void test03() {
      var registry = new DocumentStyleRegistry();
      assertThatThrownBy(() -> sut.execute(registry, "INVALID_KEY", 16))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Prototype not found for key: INVALID_KEY");
    }

    @Test
    @DisplayName("異常系: 不正なフォントサイズを指定した場合、IllegalArgumentExceptionが発生すること")
    void test04() {
      var registry = new DocumentStyleRegistry();
      assertThatThrownBy(() -> sut.execute(registry, "DEFAULT", 0))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("customFontSize must be greater than 0");
    }
  }
}
