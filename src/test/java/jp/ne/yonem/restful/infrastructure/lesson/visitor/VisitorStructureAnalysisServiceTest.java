package jp.ne.yonem.restful.infrastructure.lesson.visitor;

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
class VisitorStructureAnalysisServiceTest {

  @InjectMocks private VisitorStructureAnalysisService sut;

  @Mock private FileSystemElement mockElement;

  @Mock private SizeCalculationVisitor mockVisitor;

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: サービスを実行した際、要素がビジターを受け入れ、サイズが返ること")
    void test01() {
      // 準備
      when(mockVisitor.getTotalSize()).thenReturn(450);

      // 実行
      var result = sut.execute(mockElement, mockVisitor);

      // 検証
      assertThat(result).isEqualTo(450);
      verify(mockElement, times(1)).accept(mockVisitor);
      verify(mockVisitor, times(1)).getTotalSize();
    }

    @Test
    @DisplayName("正常系: 実体構造に対してサイズ集計ビジターが正しく状態を集計できること")
    void test02() {
      // 実構造を用いた結合仕様補完
      var root = new VisitorFolder("root");
      var file1 = new VisitorFile("a.txt", 100);
      var file2 = new VisitorFile("b.txt", 250);
      root.add(file1);
      root.add(file2);

      var realVisitor = new SizeCalculationVisitor();

      // 実行
      var result = sut.execute(root, realVisitor);

      // 検証: 100 + 250 = 350
      assertThat(result).isEqualTo(350);
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: 要素がnullの場合、NullPointerExceptionが発生すること")
    void test01() {
      assertThatThrownBy(() -> sut.execute(null, mockVisitor))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("element must not be null");
    }

    @Test
    @DisplayName("異常系: ビジターがnullの場合、NullPointerExceptionが発生すること")
    void test02() {
      assertThatThrownBy(() -> sut.execute(mockElement, null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("visitor must not be null");
    }
  }
}
