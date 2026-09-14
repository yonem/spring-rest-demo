package jp.ne.yonem.restful.infrastructure.lesson2.bridge;

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
class NotificationBridgeWorkflowServiceTest {

  @InjectMocks private NotificationBridgeWorkflowService sut;

  @Mock private NotificationAbstraction notification;

  @Mock private MessageSenderImplementor implementor;

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: モック通知オブジェクト経由で正しくメッセージが送信できること")
    void test01() {
      when(notification.notify("件名", "内容")).thenReturn("送信完了");

      var result = sut.execute(notification, "件名", "内容");

      assertThat(result).isEqualTo("送信完了");
      verify(notification, times(1)).notify("件名", "内容");
    }

    @Test
    @DisplayName("正常系: 通常通知 × Email送信用実装の組み合わせが正常に動作すること")
    void test02() {
      var emailImplementor = new EmailSenderImplementor();
      var normalNotification = new NormalNotification(emailImplementor);

      var result = sut.execute(normalNotification, "お知らせ", "定期メンテナンス");

      assertThat(result).isEqualTo("[Email] お知らせ: 定期メンテナンス");
    }

    @Test
    @DisplayName("正常系: 緊急通知 × Push送信用実装の組み合わせが正常に動作すること")
    void test03() {
      var pushImplementor = new PushNotificationSenderImplementor();
      var urgentNotification = new UrgentNotification(pushImplementor);

      var result = sut.execute(urgentNotification, "警報", "システム障害");

      assertThat(result).isEqualTo("[Push] [URGENT] 警報: システム障害");
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: 通知オブジェクトがnullの場合、NullPointerExceptionが発生すること")
    void test01() {
      assertThatThrownBy(() -> sut.execute(null, "件名", "内容"))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("notification must not be null");
    }

    @Test
    @DisplayName("異常系: タイトルがnullの場合、NullPointerExceptionが発生すること")
    void test02() {
      assertThatThrownBy(() -> sut.execute(notification, null, "内容"))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("title must not be null");
    }

    @Test
    @DisplayName("異常系: 本文がnullの場合、NullPointerExceptionが発生すること")
    void test03() {
      assertThatThrownBy(() -> sut.execute(notification, "件名", null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("body must not be null");
    }

    @Test
    @DisplayName("異常系: Notification構築時にimplementorがnullの場合、NullPointerExceptionが発生すること")
    void test04() {
      assertThatThrownBy(() -> new NormalNotification(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("implementor must not be null");
    }
  }
}
