package jp.ne.yonem.restful.infrastructure.lesson.interpreter;

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
class ExpressionEvaluationServiceTest {

  @InjectMocks private ExpressionEvaluationService sut;

  @Mock private Expression mockExpression;

  @Mock private ExpressionContext mockContext;

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: モック構文木を評価し、期待通りの計算結果が返ること")
    void test01() {
      // 準備
      when(mockExpression.interpret(mockContext)).thenReturn(42);

      // 実行
      var result = sut.execute(mockExpression, mockContext);

      // 検証
      assertThat(result).isEqualTo(42);
      verify(mockExpression, times(1)).interpret(mockContext);
    }

    @Test
    @DisplayName("正常系: (10 + 20) - 5 の実構造式ツリーを正しく評価して 25 が返ること")
    void test02() {
      // 実構造による評価テスト: (10 + 20) - 5
      var ten = new NumberExpression(10);
      var twenty = new NumberExpression(20);
      var five = new NumberExpression(5);

      var add = new AddExpression(ten, twenty);
      var expressionTree = new SubtractExpression(add, five);

      var realContext = new ExpressionContext();

      // 実行
      var result = sut.execute(expressionTree, realContext);

      // 検証: (10 + 20) - 5 = 25
      assertThat(result).isEqualTo(25);
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: 構文木がnullの場合、NullPointerExceptionが発生すること")
    void test01() {
      assertThatThrownBy(() -> sut.execute(null, mockContext))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("expression must not be null");
    }

    @Test
    @DisplayName("異常系: コンテキストがnullの場合、NullPointerExceptionが発生すること")
    void test02() {
      assertThatThrownBy(() -> sut.execute(mockExpression, null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("context must not be null");
    }
  }
}
