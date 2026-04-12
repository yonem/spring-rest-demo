package jp.ne.yonem.restful.infrastructure.lesson.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class TextAutoFormatServiceTest {

  @InjectMocks private TextAutoFormatService sut;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Nested
  @DisplayName("正常系: 自動加工の検証")
  class SuccessTests {

    @Test
    @DisplayName("test01: [U]で始まる場合、大文字化されること")
    void test01() {
      var result = sut.execute("[U]hello");
      assertThat(result).isEqualTo("[U]HELLO");
    }

    @Test
    @DisplayName("test02: 空白で始まる場合、トリムされること")
    void test02() {
      var result = sut.execute("  gap  ");
      assertThat(result).isEqualTo("gap");
    }

    @Test
    @DisplayName("test03: 条件に合致しない場合、そのまま返ること")
    void test03() {
      var result = sut.execute("no_change");
      assertThat(result).isEqualTo("no_change");
    }
  }
}
