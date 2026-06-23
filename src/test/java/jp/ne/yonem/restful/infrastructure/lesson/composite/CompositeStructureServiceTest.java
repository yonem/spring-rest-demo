package jp.ne.yonem.restful.infrastructure.lesson.composite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CompositeStructureServiceTest {

  @InjectMocks private CompositeStructureService sut;

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: 単一ファイルのサイズが正しく取得できること")
    void test01() {
      var file = new CompositeFile("test.txt", 150);
      var result = sut.calculateTotalSize(file);
      assertThat(result).isEqualTo(150);
    }

    @Test
    @DisplayName("正常系: フォルダ構造の総サイズが再帰的に正しく集計されること")
    void test02() {
      var root = new CompositeFolder("root");
      var subFolder = new CompositeFolder("sub");

      root.add(new CompositeFile("file1.txt", 100));
      subFolder.add(new CompositeFile("file2.txt", 200));
      root.add(subFolder);

      var result = sut.calculateTotalSize(root);

      assertThat(result).isEqualTo(300);
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: コンポーネントがnullの場合、NullPointerExceptionが発生すること")
    void test01() {
      assertThatThrownBy(() -> sut.calculateTotalSize(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("component must not be null");
    }
  }
}
