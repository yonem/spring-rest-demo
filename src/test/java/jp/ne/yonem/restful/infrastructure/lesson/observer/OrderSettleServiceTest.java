package jp.ne.yonem.restful.infrastructure.lesson.observer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import jp.ne.yonem.restful.infrastructure.lesson.observer.OrderSettleService.OrderEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

class OrderSettleServiceTest {

  @Mock private ApplicationEventPublisher publisher;

  @InjectMocks private OrderSettleService sut;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Nested
  @DisplayName("正常系: イベント発行の検証")
  class SuccessTests {

    @Test
    @DisplayName("test01: execute実行時にイベントがパブリッシュされること")
    void test01() {
      sut.execute("ORD-001", 100L, 5000);

      // publisherが呼ばれていれば、Spring環境下ではListenerに通知されます
      verify(publisher, times(1)).publishEvent(any(OrderEvent.class));
    }
  }
}
