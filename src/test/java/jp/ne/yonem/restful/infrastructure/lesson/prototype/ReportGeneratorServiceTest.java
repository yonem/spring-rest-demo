package jp.ne.yonem.restful.infrastructure.lesson.prototype;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportGeneratorServiceTest {

  @InjectMocks private ReportGeneratorService sut;

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: 雛形をコピーし、指定した言語に変更されたレポートが生成されること")
    void test01() {
      var base = new ReportTemplate("月次報告", "Header", "Footer", "Japanese");

      var result = sut.execute(base, "English");

      assertThat(result.title()).isEqualTo("月次報告"); // 元の値を保持
      assertThat(result.language()).isEqualTo("English"); // 指定した値に変更
      assertThat(result).isNotSameAs(base); // 別のインスタンスであること
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: 雛形がnullの場合、NullPointerExceptionが発生すること")
    void test01() {
      assertThatThrownBy(() -> sut.execute(null, "Japanese"))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("baseTemplate must not be null");
    }

    @Test
    @DisplayName("異常系: 言語がnullの場合、NullPointerExceptionが発生すること")
    void test02() {
      var base = new ReportTemplate("T", "H", "F", "L");
      assertThatThrownBy(() -> sut.execute(base, null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("targetLanguage must not be null");
    }
  }
}
