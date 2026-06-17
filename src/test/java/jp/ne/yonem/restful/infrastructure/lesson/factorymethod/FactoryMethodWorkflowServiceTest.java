package jp.ne.yonem.restful.infrastructure.lesson.factorymethod;

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
class FactoryMethodWorkflowServiceTest {

  @InjectMocks private FactoryMethodWorkflowService sut;

  @Mock private AbstractMessageFactory factory;

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: 工場からメッセージが正しく生成され、その結果が返ること")
    void test01() {
      var expectedMessage = "[Mock] メッセージ内容";
      when(factory.prepareMessage()).thenReturn(expectedMessage);

      var result = sut.execute(factory);

      assertThat(result).isEqualTo(expectedMessage);
      verify(factory, times(1)).prepareMessage();
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: 工場インスタンスがnullの場合、NullPointerExceptionが発生すること")
    void test01() {
      assertThatThrownBy(() -> sut.execute(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("factory must not be null");
    }
  }
}
