package jp.ne.yonem.restful.infrastructure.lesson2.adapter;

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
class PaymentSettleWorkflowServiceTest {

  @InjectMocks private PaymentSettleWorkflowService sut;

  @Mock private PaymentProcessor processor;

  @Mock private LegacyPaymentGateway legacyGateway;

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: PaymentProcessor経由で決済が正しく実行されること")
    void test01() {
      when(processor.processPayment("ACC123", 5000))
          .thenReturn("SUCCESS: Account[ACC123] Amount[5000]");

      var result = sut.execute(processor, "ACC123", 5000);

      assertThat(result).isEqualTo("SUCCESS: Account[ACC123] Amount[5000]");
      verify(processor, times(1)).processPayment("ACC123", 5000);
    }

    @Test
    @DisplayName("正常系: LegacyPaymentAdapterを経由してレガシーAPIの決済が正常終了すること")
    void test02() {
      when(legacyGateway.executeTransaction(3000, "USER_999")).thenReturn(200);

      var adapter = new LegacyPaymentAdapter(legacyGateway);
      var result = sut.execute(adapter, "USER_999", 3000);

      assertThat(result).isEqualTo("SUCCESS: Account[USER_999] Amount[3000]");
      verify(legacyGateway, times(1)).executeTransaction(3000, "USER_999");
    }

    @Test
    @DisplayName("正常系: レガシーAPIがエラーコードを返した場合に失敗メッセージが出力されること")
    void test03() {
      when(legacyGateway.executeTransaction(1000, "USER_999")).thenReturn(500);

      var adapter = new LegacyPaymentAdapter(legacyGateway);
      var result = sut.execute(adapter, "USER_999", 1000);

      assertThat(result).isEqualTo("FAILURE: Account[USER_999]");
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: プロセッサーがnullの場合、NullPointerExceptionが発生すること")
    void test01() {
      assertThatThrownBy(() -> sut.execute(null, "ACC123", 1000))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("processor must not be null");
    }

    @Test
    @DisplayName("異常系: アカウントIDがnullの場合、NullPointerExceptionが発生すること")
    void test02() {
      assertThatThrownBy(() -> sut.execute(processor, null, 1000))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("accountId must not be null");
    }

    @Test
    @DisplayName("異常系: アダプター生成時にレガシーAPIがnullの場合、NullPointerExceptionが発生すること")
    void test03() {
      assertThatThrownBy(() -> new LegacyPaymentAdapter(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("legacyGateway must not be null");
    }

    @Test
    @DisplayName("異常系: 金額が0以下の場合、IllegalArgumentExceptionが発生すること")
    void test04() {
      var adapter = new LegacyPaymentAdapter(legacyGateway);

      assertThatThrownBy(() -> adapter.processPayment("ACC123", 0))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("amountYen must be greater than 0");
    }
  }
}
