package jp.ne.yonem.restful.infrastructure.lesson.iterator;

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
class IteratorAggregationServiceTest {

  @InjectMocks private IteratorAggregationService sut;

  @Mock private CustomAggregate aggregate;

  @Mock private CustomIterator iterator;

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: 集合体からイテレータを介してすべてのタイトルが取得できること")
    void test01() {
      // 準備
      var book1 = new LessonBook("Java21入門");
      var book2 = new LessonBook("Design Patterns");

      when(aggregate.iterator()).thenReturn(iterator);
      // 1回目の走査、2回目の走査、3回目の終了判定をモック
      when(iterator.hasNext()).thenReturn(true, true, false);
      when(iterator.next()).thenReturn(book1, book2);

      // 実行
      var result = sut.execute(aggregate);

      // 検証
      assertThat(result).containsExactly("Java21入門", "Design Patterns");
      verify(aggregate, times(1)).iterator();
      verify(iterator, times(3)).hasNext();
      verify(iterator, times(2)).next();
    }

    @Test
    @DisplayName("正常系: 集合体が空の場合、空のリストが返ること")
    void test02() {
      when(aggregate.iterator()).thenReturn(iterator);
      when(iterator.hasNext()).thenReturn(false);

      var result = sut.execute(aggregate);

      assertThat(result).isEmpty();
      verify(iterator, never()).next();
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: 集合体がnullの場合、NullPointerExceptionが発生すること")
    void test01() {
      assertThatThrownBy(() -> sut.execute(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("aggregate must not be null");
    }
  }
}
