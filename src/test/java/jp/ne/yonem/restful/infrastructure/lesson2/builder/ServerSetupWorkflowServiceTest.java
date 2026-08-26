package jp.ne.yonem.restful.infrastructure.lesson2.builder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServerSetupWorkflowServiceTest {

  @InjectMocks private ServerSetupWorkflowService sut;

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: Builderでパラメータを設定し正しく構成オブジェクトが生成されること")
    void test01() {
      var result = sut.execute("api.example.com", 443, 500, true);

      assertThat(result).isEqualTo("Server[api.example.com:443] Connections:500 SSL:true");
    }

    @Test
    @DisplayName("正常系: Builderでデフォルト値をそのまま採用して構築できること")
    void test02() {
      var config = ServerConfiguration.builder("localhost").build();

      assertThat(config.getHostName()).isEqualTo("localhost");
      assertThat(config.getPort()).isEqualTo(8080);
      assertThat(config.getMaxConnections()).isEqualTo(100);
      assertThat(config.isSslEnabled()).isFalse();
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: ホスト名がnullの場合、NullPointerExceptionが発生すること")
    void test01() {
      assertThatThrownBy(() -> sut.execute(null, 8080, 100, false))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("hostName must not be null");
    }

    @Test
    @DisplayName("異常系: 不正なポート番号を指定した場合、IllegalArgumentExceptionが発生すること")
    void test02() {
      assertThatThrownBy(() -> ServerConfiguration.builder("localhost").port(0))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("port must be between 1 and 65535");
    }

    @Test
    @DisplayName("異常系: 不正な最大接続数を指定した場合、IllegalArgumentExceptionが発生すること")
    void test03() {
      assertThatThrownBy(() -> ServerConfiguration.builder("localhost").maxConnections(-1))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("maxConnections must be greater than 0");
    }
  }
}
