package jp.ne.yonem.restful.infrastructure.lesson2.abstractfactory;

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
class PlatformNotificationWorkflowServiceTest {

  @InjectMocks private PlatformNotificationWorkflowService sut;

  @Mock private NotificationComponentFactory factory;

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: モック工場の生成結果を利用して正しく通知処理が実行されること")
    void test01() {

      // 準備: sealed インターフェースの @Mock ではなく、具象クラスを生成して返す
      var card = new WebNotificationCard();
      var sender = new WebNotificationSender();

      when(factory.createCard()).thenReturn(card);
      when(factory.createSender()).thenReturn(sender);

      // 実行
      var result = sut.execute(factory, "テストメッセージ");

      // 検証
      assertThat(result).isEqualTo("<div class='web-card'>Web通知</div> -> [WebPush配信] テストメッセージ");
      verify(factory, times(1)).createCard();
      verify(factory, times(1)).createSender();
    }

    @Test
    @DisplayName("正常系: WebComponentFactory（実体）を利用した際、Web用コンポーネントセットが生成されること")
    void test02() {
      var webFactory = new WebComponentFactory();

      var result = sut.execute(webFactory, "障害通知");

      assertThat(result).isEqualTo("<div class='web-card'>Web通知</div> -> [WebPush配信] 障害通知");
    }

    @Test
    @DisplayName("正常系: MobileComponentFactory（実体）を利用した際、Mobile用コンポーネントセットが生成されること")
    void test03() {
      var mobileFactory = new MobileComponentFactory();

      var result = sut.execute(mobileFactory, "障害通知");

      assertThat(result)
          .isEqualTo("{ \"type\": \"mobile_card\", \"title\": \"Mobile通知\" } -> [APNs/FCM配信] 障害通知");
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: 工場インスタンスがnullの場合、NullPointerExceptionが発生すること")
    void test01() {
      assertThatThrownBy(() -> sut.execute(null, "message"))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("factory must not be null");
    }

    @Test
    @DisplayName("異常系: メッセージがnullの場合、NullPointerExceptionが発生すること")
    void test02() {
      assertThatThrownBy(() -> sut.execute(factory, null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("message must not be null");
    }
  }
}
