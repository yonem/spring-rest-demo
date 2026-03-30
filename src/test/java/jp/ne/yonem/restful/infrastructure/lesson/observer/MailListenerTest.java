package jp.ne.yonem.restful.infrastructure.lesson.observer;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class MailListenerTest {

  @InjectMocks private MailListener sut;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Nested
  @DisplayName("正常系: メール送信リスナーの検証")
  class SuccessTests {

    @Test
    @DisplayName("test01: イベントを受け取った際に正しく処理が実行されること")
    void test01() {
      // 準備: 引数となるイベントレコードを作成
      var event = new OrderSettleService.OrderEvent("ORD001", 123L, 5000, LocalDateTime.now());

      // 実行: メソッドを直接呼び出す（ユニットテストの基本）
      sut.onOrderComplete(event);

      // 検証: ログ出力の確認（必要に応じてLoggerをモック化してverifyする等）
    }
  }
}
