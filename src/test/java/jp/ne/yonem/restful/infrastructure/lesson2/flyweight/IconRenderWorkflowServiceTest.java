package jp.ne.yonem.restful.infrastructure.lesson2.flyweight;

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
class IconRenderWorkflowServiceTest {

  @InjectMocks private IconRenderWorkflowService sut;

  @Mock private IconFactory factory;

  @Mock private IconFlyweight flyweight;

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: モック工場とFlyweightを用いて正しく描画結果が返ること")
    void test01() {
      when(factory.getIcon("USER")).thenReturn(flyweight);
      when(flyweight.draw(10, 20, "BLUE")).thenReturn("Icon[USER] at (10, 20) with color[BLUE]");

      var result = sut.execute(factory, "USER", 10, 20, "BLUE");

      assertThat(result).isEqualTo("Icon[USER] at (10, 20) with color[BLUE]");
      verify(factory, times(1)).getIcon("USER");
      verify(flyweight, times(1)).draw(10, 20, "BLUE");
    }

    @Test
    @DisplayName("正常系: IconFactoryで同種のアイコンを取得した際、同一インスタンスが再利用されること")
    void test02() {
      var realFactory = new IconFactory();

      // 同じ "STAR" アイコンを複数回取得
      var icon1 = realFactory.getIcon("STAR");
      var icon2 = realFactory.getIcon("STAR");

      // インスタンスが参照一致（==）し、キャッシュサイズが1であることを検証
      assertThat(icon1).isSameAs(icon2);
      assertThat(realFactory.getCacheSize()).isEqualTo(1);

      var result = sut.execute(realFactory, "STAR", 100, 200, "GOLD");
      assertThat(result).isEqualTo("Icon[STAR] at (100, 200) with color[GOLD]");
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: 工場がnullの場合、NullPointerExceptionが発生すること")
    void test01() {
      assertThatThrownBy(() -> sut.execute(null, "USER", 0, 0, "RED"))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("factory must not be null");
    }

    @Test
    @DisplayName("異常系: アイコン種別がnullの場合、NullPointerExceptionが発生すること")
    void test02() {
      assertThatThrownBy(() -> sut.execute(factory, null, 0, 0, "RED"))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("iconType must not be null");
    }

    @Test
    @DisplayName("異常系: 色がnullの場合、NullPointerExceptionが発生すること")
    void test03() {
      assertThatThrownBy(() -> sut.execute(factory, "USER", 0, 0, null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("color must not be null");
    }
  }
}
