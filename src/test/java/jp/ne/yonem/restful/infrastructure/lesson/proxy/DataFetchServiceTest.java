package jp.ne.yonem.restful.infrastructure.lesson.proxy;

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
class DataFetchServiceTest {

  @InjectMocks private DataFetchService sut;

  @Mock private DataProvider provider;

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: プロバイダーからデータが取得できること")
    void test01() {
      var expected = "test data";
      when(provider.fetchData()).thenReturn(expected);

      var result = sut.execute(provider);

      assertThat(result).isEqualTo(expected);
      verify(provider, times(1)).fetchData();
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: プロバイダーがnullの場合、例外が発生すること")
    void test01() {
      assertThatThrownBy(() -> sut.execute(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("provider must not be null");
    }
  }
}
