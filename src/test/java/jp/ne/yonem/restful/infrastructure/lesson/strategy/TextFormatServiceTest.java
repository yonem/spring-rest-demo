package jp.ne.yonem.restful.infrastructure.lesson.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class TextFormatServiceTest {

  @InjectMocks private TextFormatService sut;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Nested
  @DisplayName("正常系: 文字列加工の検証")
  class SuccessTests {

    @Test
    @DisplayName("test01: TRIMとUPPERを順番に適用できること")
    void test01() {
      var result =
          sut.execute("  hello  ", List.of(TextProcessStrategy.TRIM, TextProcessStrategy.UPPER));
      assertThat(result).isEqualTo("HELLO");
    }

    @Test
    @DisplayName("test02: 戦略リストが空の場合は元の文字列が返ること")
    void test02() {
      var result = sut.execute("keep", List.of());
      assertThat(result).isEqualTo("keep");
    }
  }

  @Nested
  @DisplayName("異常系: 入力値の検証")
  class ExceptionTests {

    @Test
    @DisplayName("test01: 入力文字列がnullの場合はnullを返すこと")
    void test01() {
      var result = sut.execute(null, List.of(TextProcessStrategy.UPPER));
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("test02: 戦略リストがnullの場合はエラーにならず元の文字列を返すこと")
    void test02() {
      var result = sut.execute("hello", null);
      assertThat(result).isEqualTo("hello");
    }
  }
}
