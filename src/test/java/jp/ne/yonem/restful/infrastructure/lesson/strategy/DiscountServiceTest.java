package jp.ne.yonem.restful.infrastructure.lesson.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class DiscountServiceTest {

  @InjectMocks private DiscountService sut;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Nested
  @DisplayName("正常系: 割引計算の検証")
  class SuccessTests {

    @Test
    @DisplayName("test01: GOLDランクの場合、20%引きされること")
    void test01() {
      var result = sut.execute("GOLD", 10000);
      assertThat(result).isEqualTo(8000);
    }

    @Test
    @DisplayName("test02: SILVERランクの場合、10%引きされること")
    void test02() {
      var result = sut.execute("SILVER", 10000);
      assertThat(result).isEqualTo(9000);
    }

    @Test
    @DisplayName("test03: 該当ランクがない場合、定価であること")
    void test03() {
      var result = sut.execute("BRONZE", 10000);
      assertThat(result).isEqualTo(10000);
    }
  }

  @Nested
  @DisplayName("異常系: パラメータ不正の検証")
  class ExceptionTests {

    @Test
    @DisplayName("test01: ランクがnullの場合、定価であること")
    void test01() {
      var result = sut.execute(null, 10000);
      assertThat(result).isEqualTo(10000);
    }
  }
}
