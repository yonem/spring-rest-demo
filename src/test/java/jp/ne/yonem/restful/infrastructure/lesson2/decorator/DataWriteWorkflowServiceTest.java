package jp.ne.yonem.restful.infrastructure.lesson2.decorator;

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
class DataWriteWorkflowServiceTest {

  @InjectMocks private DataWriteWorkflowService sut;

  @Mock private DataWriter writer;

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: モックのDataWriter経由で正しくデータが処理されること")
    void test01() {
      when(writer.writeData("hello")).thenReturn("hello");

      var result = sut.execute(writer, "hello");

      assertThat(result).isEqualTo("hello");
      verify(writer, times(1)).writeData("hello");
    }

    @Test
    @DisplayName("正常系: PlainDataWriterでそのままのデータが出力されること")
    void test02() {
      var plainWriter = new PlainDataWriter();

      var result = sut.execute(plainWriter, "Hello World");

      assertThat(result).isEqualTo("Hello World");
    }

    @Test
    @DisplayName("正常系: 暗号化および圧縮デコレーターを段階的に重ねて機能拡張できること")
    void test03() {
      var decoratedWriter =
          new EncryptionDecorator(new CompressionDecorator(new PlainDataWriter()));

      var result = sut.execute(decoratedWriter, "SecretData");

      assertThat(result).isEqualTo("[COMPRESSED]U2VjcmV0RGF0YQ==");
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: writerがnullの場合、NullPointerExceptionが発生すること")
    void test01() {
      assertThatThrownBy(() -> sut.execute(null, "data"))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("writer must not be null");
    }

    @Test
    @DisplayName("異常系: dataがnullの場合、NullPointerExceptionが発生すること")
    void test02() {
      assertThatThrownBy(() -> sut.execute(writer, null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("data must not be null");
    }

    @Test
    @DisplayName("異常系: デコレーター生成時にwrappeeがnullの場合、NullPointerExceptionが発生すること")
    void test03() {
      assertThatThrownBy(() -> new EncryptionDecorator(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("wrappee must not be null");
    }
  }
}
