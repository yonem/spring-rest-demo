package jp.ne.yonem.restful.infrastructure.lesson;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import jp.ne.yonem.restful.infrastructure.lesson.StockSettleService.StockEvent;
import jp.ne.yonem.restful.infrastructure.lesson.StockSettleService.StockObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class StockSettleServiceTest {

  @Mock private StockObserver stockObserver;
  @InjectMocks private StockSettleService sut;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Nested
  @DisplayName("正常系: 在庫精算の検証")
  class SuccessTests {

    @Test
    @DisplayName("test01: 在庫が正しく減算され通知が実行されること")
    void test01() {
      sut.addObserver(stockObserver);

      sut.execute(30);

      verify(stockObserver, times(1)).onUpdate(any(StockEvent.class));
    }

    @Test
    @DisplayName("test02: 在庫が0以下にならないこと")
    void test02() {
      sut.addObserver(stockObserver);

      sut.execute(150); // 初期値100を超える減算

      verify(stockObserver, times(1)).onUpdate(argThat(event -> event.currentStock() == 0));
    }
  }

  @Nested
  @DisplayName("異常系: 特殊ケースの検証")
  class ExceptionTests {

    @Test
    @DisplayName("test01: オブザーバーが登録されていない場合でもエラーにならないこと")
    void test01() {
      // オブザーバーを登録せずに実行
      sut.execute(10);

      verify(stockObserver, never()).onUpdate(any());
    }

    @Test
    @DisplayName("test02: nullのオブザーバー登録が無視されること")
    void test02() {
      sut.addObserver(null);

      sut.execute(10);

      verify(stockObserver, never()).onUpdate(any());
    }
  }
}
