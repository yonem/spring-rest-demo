package jp.ne.yonem.restful.infrastructure.lesson.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class DiscountSettleServiceTest {

  @InjectMocks private DiscountSettleService sut;

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
      var result = sut.execute(MemberRank.GOLD, 10000);
      assertThat(result).isEqualTo(8000);
    }

    @Test
    @DisplayName("test02: STANDARDランクの場合、金額が変わらないこと")
    void test02() {
      var result = sut.execute(MemberRank.STANDARD, 10000);
      assertThat(result).isEqualTo(10000);
    }
  }

  @Nested
  @DisplayName("異常系: パラメータ不正の検証")
  class ExceptionTests {

    @Test
    @DisplayName("test01: rankがnullの場合、STANDARDとして扱われ金額が変わらないこと")
    void test01() {
      var result = sut.execute(null, 10000);
      assertThat(result).isEqualTo(10000);
    }
  }
}
