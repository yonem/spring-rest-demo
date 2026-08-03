package jp.ne.yonem.restful.infrastructure.lesson2.singleton;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SystemConfigRegistryServiceTest {

  @InjectMocks private SystemConfigRegistryService sut;

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: 複数回 getInstance を呼んでも常に同一のインスタンスが返ること")
    void test01() {
      var instance1 = AppConfigRegistry.getInstance();
      var instance2 = AppConfigRegistry.getInstance();

      // 参照が完全に一致していること（==）
      assertThat(instance1).isSameAs(instance2);
    }

    @Test
    @DisplayName("正常系: サービス経由で登録した値がシングルトンに保存され取得できること")
    void test02() {
      var key = "max.connections";
      var value = "100";

      var result = sut.registerAndGet(key, value);

      assertThat(result).isEqualTo("100");
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: キーがnullの場合、NullPointerExceptionが発生すること")
    void test01() {
      assertThatThrownBy(() -> sut.registerAndGet(null, "value"))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("key must not be null");
    }

    @Test
    @DisplayName("異常系: 値がnullの場合、NullPointerExceptionが発生すること")
    void test02() {
      assertThatThrownBy(() -> sut.registerAndGet("key", null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("value must not be null");
    }
  }
}
