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

class MessageFullAopServiceTest {

  @Mock private ApplicationEventPublisher publisher;

  @InjectMocks private MessageFullAopService sut;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: [U]で始まるメッセージが加工され、イベントが発行されること")
    void test01() {
      var result = sut.execute("[U]java");

      var captor = ArgumentCaptor.forClass(MessageProcessedEvent.class);
      verify(publisher).publishEvent(captor.capture());

      assertThat(result).isEqualTo("JAVA");
      assertThat(captor.getValue().content()).isEqualTo("JAVA");
    }
  }

  @Nested
  class ExceptionTests {
    @Test
    @DisplayName("異常系: 入力がnullの場合、空文字として処理されイベントが発行されること")
    void test01() {
      var result = sut.execute("");

      verify(publisher).publishEvent(any(MessageProcessedEvent.class));
      assertThat(result).isEmpty();
    }
  }
}
