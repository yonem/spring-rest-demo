package jp.ne.yonem.restful.infrastructure.lesson.templatemethod;

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
class TemplateMethodWorkflowServiceTest {

  @InjectMocks private TemplateMethodWorkflowService sut;

  @Mock private AbstractDataProcessor processor;

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: プロセッサーの処理が全体の骨組み通りに呼び出され、加工結果が返ること")
    void test01() {
      // AbstractDataProcessorのprocessメソッドはfinalなので、
      // 本来の挙動（骨組み）をそのまま通すためにスパイや実体ベース、あるいは通常のモックのスタブ定義を行います。
      var source = "input_data";
      var expected = "PROCESSED_DATA";
      when(processor.process(source)).thenReturn(expected);

      var result = sut.execute(processor, source);

      assertThat(result).isEqualTo(expected);
      verify(processor, times(1)).process(source);
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: プロセッサーがnullの場合、NullPointerExceptionが発生すること")
    void test01() {
      assertThatThrownBy(() -> sut.execute(null, "data"))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("processor must not be null");
    }

    @Test
    @DisplayName("異常系: データ元がnullの場合、NullPointerExceptionが発生すること")
    void test02() {
      assertThatThrownBy(() -> sut.execute(processor, null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("source must not be null");
    }
  }
}
