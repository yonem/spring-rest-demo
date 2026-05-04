package jp.ne.yonem.restful.infrastructure.lesson.chain;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class MessageDispatchServiceTest {

  @Mock private MessageSender primarySender;

  @Mock private MessageSender specificSender;

  private MessageDispatchService sut;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    // 明示的にコンストラクタでモックを渡し、注入ミスを防ぐ
    sut = new MessageDispatchService(primarySender, specificSender);
  }

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: 通常のメッセージがPrimaryのみに送信されること")
    void test01() {
      sut.execute("hello");

      verify(primarySender, times(1)).send("hello");
      verify(specificSender, never()).send(anyString());
    }

    @Test
    @DisplayName("正常系: URGENTを含むメッセージが両方のSenderに送信されること")
    void test02() {
      sut.execute("URGENT message");

      // 別々のモックインスタンスとして検証される
      verify(primarySender, times(1)).send("URGENT message");
      verify(specificSender, times(1)).send("URGENT message");
    }
  }
}
