package jp.ne.yonem.restful.infrastructure.lesson2.chainofresponsibility;

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
class OrderValidationWorkflowServiceTest {

  @InjectMocks private OrderValidationWorkflowService sut;

  @Mock private OrderHandler handler;

  @Mock private OrderContext context;

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: モックハンドラー経由で正しく検証結果が取得できること")
    void test01() {
      when(handler.handle(context)).thenReturn("SUCCESS: Order processed");

      var result = sut.execute(handler, context);

      assertThat(result).isEqualTo("SUCCESS: Order processed");
      verify(handler, times(1)).handle(context);
    }

    @Test
    @DisplayName("正常系: 在庫・与信の両チェックを通過し処理が成功すること")
    void test02() {
      var stockHandler = new StockCheckHandler();
      var creditHandler = new CreditCheckHandler();
      stockHandler.setNext(creditHandler);

      var orderContext = new OrderContext("ORD_001", 1000);

      var result = sut.execute(stockHandler, orderContext);

      assertThat(result).isEqualTo("SUCCESS: Order processed");
    }

    @Test
    @DisplayName("正常系: 在庫切れの場合、先頭のStockCheckHandlerで検証が拒否され後続が呼ばれないこと")
    void test03() {
      var stockHandler = new StockCheckHandler();
      var creditHandler = new CreditCheckHandler();
      stockHandler.setNext(creditHandler);

      var orderContext = new OrderContext("ORD_002", 2000);
      orderContext.setStockAvailable(false); // 在庫切れ

      var result = sut.execute(stockHandler, orderContext);

      assertThat(result).isEqualTo("FAILED: Out of stock");
    }

    @Test
    @DisplayName("正常系: 在庫はOKだが与信NGの場合、2番目のCreditCheckHandlerで検証が拒否されること")
    void test04() {
      var stockHandler = new StockCheckHandler();
      var creditHandler = new CreditCheckHandler();
      stockHandler.setNext(creditHandler);

      var orderContext = new OrderContext("ORD_003", 3000);
      orderContext.setCreditApproved(false); // 与信NG

      var result = sut.execute(stockHandler, orderContext);

      assertThat(result).isEqualTo("FAILED: Credit limit exceeded");
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: ハンドラーがnullの場合、NullPointerExceptionが発生すること")
    void test01() {
      assertThatThrownBy(() -> sut.execute(null, context))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("initialHandler must not be null");
    }

    @Test
    @DisplayName("異常系: コンテキストがnullの場合、NullPointerExceptionが発生すること")
    void test02() {
      assertThatThrownBy(() -> sut.execute(handler, null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("context must not be null");
    }

    @Test
    @DisplayName("異常系: 不正な注文金額を指定した場合、IllegalArgumentExceptionが発生すること")
    void test03() {
      assertThatThrownBy(() -> new OrderContext("ORD_ERR", 0))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("amount must be greater than 0");
    }
  }
}
