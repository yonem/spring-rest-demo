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

class MessageChainServiceTest {

  @Mock private ApplicationEventPublisher publisher;

  @InjectMocks private MessageChainService sut;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: [U]で始まるメッセージが加工され、イベント発行されること")
    void test01() {
      // 実行
      sut.execute("[U]hello");

      // 検証: キャプチャを使用して型安全に値を取り出す
      var captor = ArgumentCaptor.forClass(MessageProcessedEvent.class);
      verify(publisher, times(1)).publishEvent(captor.capture());

      var actualEvent = captor.getValue();
      assertThat(actualEvent.content()).isEqualTo("HELLO");
    }

    @Test
    @DisplayName("正常系: 通常のメッセージが加工されずに、イベント発行されること")
    void test02() {
      sut.execute("valid message");

      var captor = ArgumentCaptor.forClass(MessageProcessedEvent.class);
      verify(publisher, times(1)).publishEvent(captor.capture());

      var actualEvent = captor.getValue();
      assertThat(actualEvent.content()).isEqualTo("valid message");
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: 禁止ワードが含まれる場合に[REJECTED]としてイベント発行されること")
    void test01() {
      sut.execute("this is NG_WORD");

      var captor = ArgumentCaptor.forClass(MessageProcessedEvent.class);
      verify(publisher).publishEvent(captor.capture());

      assertThat(captor.getValue().content()).isEqualTo("[REJECTED]");
    }
  }
}
