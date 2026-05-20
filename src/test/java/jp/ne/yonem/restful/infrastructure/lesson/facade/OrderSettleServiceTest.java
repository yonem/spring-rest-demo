package jp.ne.yonem.restful.infrastructure.lesson.facade;

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
class OrderSettleServiceTest {

  @InjectMocks private OrderSettleService sut;

  @Mock private OrderProcessorFacade facade;

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: ファサードが成功を返す場合、サービスも成功を返すこと")
    void test01() {
      when(facade.processOrder("P001", 1000)).thenReturn(true);

      var result = sut.execute(facade, "P001", 1000);

      assertThat(result).isTrue();
      verify(facade, times(1)).processOrder("P001", 1000);
    }

    @Test
    @DisplayName("正常系: 在庫不足等でファサードが失敗を返す場合、サービスも失敗を返すこと")
    void test02() {
      when(facade.processOrder(anyString(), anyInt())).thenReturn(false);

      var result = sut.execute(facade, "P002", 2000);

      assertThat(result).isFalse();
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: ファサードがnullの場合、NullPointerExceptionが発生すること")
    void test01() {
      assertThatThrownBy(() -> sut.execute(null, "P001", 1000))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("facade must not be null");
    }
  }
}
