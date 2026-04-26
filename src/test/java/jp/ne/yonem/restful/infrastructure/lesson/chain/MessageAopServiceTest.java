package jp.ne.yonem.restful.infrastructure.lesson.chain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

class MessageAopServiceTest {

  @Mock private ApplicationEventPublisher publisher;

  @InjectMocks private MessageAopService sut;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: [U]で始まるメッセージが加工され、イベント発行されること")
    void test01() {
      var result = sut.execute("[U]hello");

      var captor = ArgumentCaptor.forClass(MessageProcessedEvent.class);
      verify(publisher).publishEvent(captor.capture());

      assertThat(result).isEqualTo("HELLO");
      assertThat(captor.getValue().content()).isEqualTo("HELLO");
    }
  }

  @Nested
  class ExceptionTests {
    // AOP自体のテスト（検閲）は別途 Aspect 単体のテストで行うのが一般的です。
    // ここではサービス単体の責務（加工と通知）が正しく動くことを担保します。
  }
}
