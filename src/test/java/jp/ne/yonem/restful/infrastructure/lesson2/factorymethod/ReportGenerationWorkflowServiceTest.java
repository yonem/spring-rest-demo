package jp.ne.yonem.restful.infrastructure.lesson2.factorymethod;

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
class ReportGenerationWorkflowServiceTest {

  @InjectMocks private ReportGenerationWorkflowService sut;

  @Mock private AbstractReportFactory factory;

  @Mock private ReportProduct product;

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: モック工場から製品が生成され、出力結果が返ること")
    void test01() {
      // 準備
      when(factory.generate("月次売上", "売上データ")).thenReturn("出力完了");

      // 実行
      var result = sut.execute(factory, "月次売上", "売上データ");

      // 検証
      assertThat(result).isEqualTo("出力完了");
      verify(factory, times(1)).generate("月次売上", "売上データ");
    }

    @Test
    @DisplayName("正常系: PdfReportFactory（実体）を利用した際、PDF形式で出力されること")
    void test02() {
      var pdfFactory = new PdfReportFactory();

      var result = sut.execute(pdfFactory, "年次報告", "PDF本文");

      assertThat(result).isEqualTo("[PDF] Title: 年次報告, Content: PDF本文");
    }

    @Test
    @DisplayName("正常系: ExcelReportFactory（実体）を利用した際、Excel形式で出力されること")
    void test03() {
      var excelFactory = new ExcelReportFactory();

      var result = sut.execute(excelFactory, "在庫一覧", "Excel本文");

      assertThat(result).isEqualTo("[Excel] Sheet: 在庫一覧, Data: Excel本文");
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: 工場がnullの場合、NullPointerExceptionが発生すること")
    void test01() {
      assertThatThrownBy(() -> sut.execute(null, "title", "content"))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("factory must not be null");
    }

    @Test
    @DisplayName("異常系: タイトルがnullの場合、NullPointerExceptionが発生すること")
    void test02() {
      assertThatThrownBy(() -> sut.execute(factory, null, "content"))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("title must not be null");
    }

    @Test
    @DisplayName("異常系: 本文がnullの場合、NullPointerExceptionが発生すること")
    void test03() {
      assertThatThrownBy(() -> sut.execute(factory, "title", null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("content must not be null");
    }
  }
}
