package jp.ne.yonem.restful.infrastructure.lesson.mediator;

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
class MediatorCommunicationServiceTest {

  @InjectMocks private MediatorCommunicationService sut;

  @Mock private ControlTowerMediator mediator;

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: サービスを実行した際、飛行機を介してメディエーターのsendMessageが呼び出されること")
    void test01() {
      // 準備
      var aircraft = new CommercialAircraft(mediator, "ANA123");
      var message = "Entering sector";

      // 実行
      sut.execute(aircraft, message);

      // 検証: 管制塔（仲介者）へ正しくメッセージが届いているか
      verify(mediator, times(1)).sendMessage(message, aircraft);
    }

    @Test
    @DisplayName("正常系: メディエーター（実体）が発信元以外の登録された飛行機へメッセージを配信すること")
    void test02() {
      // 結合ロジックの補完テスト
      var realMediator = new ConcreteControlTowerMediator();
      var ana = new CommercialAircraft(realMediator, "ANA123");
      var jal = spy(new CommercialAircraft(realMediator, "JAL456"));

      realMediator.register(ana);
      realMediator.register(jal);

      // ANAが送信
      ana.send("Weather alert");

      // JALは受信するが、ANA自身は受信しないことの検証
      verify(jal, times(1)).receive("Weather alert");
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: 飛行機がnullの場合、NullPointerExceptionが発生すること")
    void test01() {
      assertThatThrownBy(() -> sut.execute(null, "Hello"))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("aircraft must not be null");
    }

    @Test
    @DisplayName("異常系: メッセージがnullの場合、NullPointerExceptionが発生すること")
    void test02() {
      var aircraft = new CommercialAircraft(mediator, "ANA123");
      assertThatThrownBy(() -> sut.execute(aircraft, null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("message must not be null");
    }
  }
}
