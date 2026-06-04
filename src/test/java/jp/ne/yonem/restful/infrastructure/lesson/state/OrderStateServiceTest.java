package jp.ne.yonem.restful.infrastructure.lesson.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderStateServiceTest {

  @InjectMocks private OrderStateService sut;

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: 状態遷移が実行され、コンテキストの状態が更新されること")
    void test01() {
      // 準備
      var context = new OrderContext();

      // 実行
      var result = sut.execute(context);

      // 検証
      assertThat(result).isNotNull();
      assertThat(context.getState().getStatusName()).isEqualTo("処理中");
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: コンテキストがnullの場合、NullPointerExceptionが発生すること")
    void test01() {
      assertThatThrownBy(() -> sut.execute(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("context must not be null");
    }
  }
}
