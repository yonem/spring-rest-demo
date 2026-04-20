package jp.ne.yonem.restful.infrastructure.lesson.chain;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class MessageNotificationListenerTest {

  @InjectMocks private MessageNotificationListener sut;

  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
  private final PrintStream standardOut = System.out;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    System.setOut(new PrintStream(outputStreamCaptor));
  }

  @AfterEach
  void tearDown() {
    System.setOut(standardOut);
  }

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: イベントを受信した際、コンソールにメッセージが出力されること")
    void test01() {
      var event = new MessageProcessedEvent("COMPLETE");

      sut.onMessageProcessed(event);

      assertThat(outputStreamCaptor.toString().trim()).contains("通知を受信しました: COMPLETE");
    }
  }
}
