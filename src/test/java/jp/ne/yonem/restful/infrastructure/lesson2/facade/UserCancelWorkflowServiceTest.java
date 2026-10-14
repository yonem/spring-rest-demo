package jp.ne.yonem.restful.infrastructure.lesson2.facade;

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
class UserCancelWorkflowServiceTest {

  @InjectMocks private UserCancelWorkflowService sut;

  @Mock private UserDeactivationFacade deactivationFacade;

  @Mock private SubscriptionService subscriptionService;

  @Mock private BillingService billingService;

  @Mock private UserAccountService userAccountService;

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: Facade経由で退会処理が成功すること")
    void test01() {
      when(deactivationFacade.processDeactivation("USER_001"))
          .thenReturn("SUCCESS: User[USER_001] deactivated");

      var result = sut.execute("USER_001");

      assertThat(result).isEqualTo("SUCCESS: User[USER_001] deactivated");
      verify(deactivationFacade, times(1)).processDeactivation("USER_001");
    }

    @Test
    @DisplayName("正常系: Facade実体を利用し、サブシステムが正しく連携して退会が完了すること")
    void test02() {
      when(billingService.hasOutstandingBalance("USER_002")).thenReturn(false);

      var realFacade =
          new UserDeactivationFacade(subscriptionService, billingService, userAccountService);
      var service = new UserCancelWorkflowService(realFacade);

      var result = service.execute("USER_002");

      assertThat(result).isEqualTo("SUCCESS: User[USER_002] deactivated");
      verify(billingService, times(1)).hasOutstandingBalance("USER_002");
      verify(subscriptionService, times(1)).cancelSubscription("USER_002");
      verify(userAccountService, times(1)).deactivateAccount("USER_002");
    }

    @Test
    @DisplayName("正常系: 未払い金がある場合、退会処理が失敗して中断すること")
    void test03() {
      when(billingService.hasOutstandingBalance("USER_003")).thenReturn(true);

      var realFacade =
          new UserDeactivationFacade(subscriptionService, billingService, userAccountService);
      var service = new UserCancelWorkflowService(realFacade);

      var result = service.execute("USER_003");

      assertThat(result).isEqualTo("FAILED: Outstanding balance exists");
      verify(billingService, times(1)).hasOutstandingBalance("USER_003");
      // 未払い金があるため、以降のサブシステムは呼ばれないこと
      verify(subscriptionService, never()).cancelSubscription(anyString());
      verify(userAccountService, never()).deactivateAccount(anyString());
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: ユーザーIDがnullの場合、NullPointerExceptionが発生すること")
    void test01() {
      assertThatThrownBy(() -> sut.execute(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("userId must not be null");
    }

    @Test
    @DisplayName("異常系: Facade生成時にサブシステムがnullの場合、NullPointerExceptionが発生すること")
    void test02() {
      assertThatThrownBy(() -> new UserDeactivationFacade(null, billingService, userAccountService))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("subscriptionService must not be null");
    }
  }
}
