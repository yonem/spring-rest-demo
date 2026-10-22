package jp.ne.yonem.restful.infrastructure.lesson2.proxy;

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
class ReportAccessWorkflowServiceTest {

  @InjectMocks private ReportAccessWorkflowService sut;

  @Mock private ReportDataReader reader;

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: モックリーダー経由で正しくレポートが読み取れること")
    void test01() {
      when(reader.readReport("ADMIN")).thenReturn("ReportData[2026_Sales]");

      var result = sut.execute(reader, "ADMIN");

      assertThat(result).isEqualTo("ReportData[2026_Sales]");
      verify(reader, times(1)).readReport("ADMIN");
    }

    @Test
    @DisplayName("正常系: Proxy経由でADMINロールのユーザーがアクセスに成功すること")
    void test02() {
      var proxy = new ProxyReportDataReader("2026_Financial");

      var result = sut.execute(proxy, "ADMIN");

      assertThat(result).isEqualTo("ReportData[2026_Financial]");
    }

    @Test
    @DisplayName("正常系: Proxy経由で非ADMINロールのユーザーがアクセス拒否されること")
    void test03() {
      var proxy = new ProxyReportDataReader("2026_Financial");

      var result = sut.execute(proxy, "USER");

      assertThat(result).isEqualTo("ACCESS DENIED: Required ADMIN role");
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: リーダーがnullの場合、NullPointerExceptionが発生すること")
    void test01() {
      assertThatThrownBy(() -> sut.execute(null, "ADMIN"))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("reader must not be null");
    }

    @Test
    @DisplayName("異常系: ロールがnullの場合、NullPointerExceptionが発生すること")
    void test02() {
      assertThatThrownBy(() -> sut.execute(reader, null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("userRole must not be null");
    }

    @Test
    @DisplayName("異常系: レポート名がnullでProxyを生成した場合、NullPointerExceptionが発生すること")
    void test03() {
      assertThatThrownBy(() -> new ProxyReportDataReader(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("reportName must not be null");
    }
  }
}
