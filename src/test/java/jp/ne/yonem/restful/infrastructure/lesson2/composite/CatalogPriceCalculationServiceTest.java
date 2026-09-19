package jp.ne.yonem.restful.infrastructure.lesson2.composite;

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
class CatalogPriceCalculationServiceTest {

  @InjectMocks private CatalogPriceCalculationService sut;

  @Mock private CatalogComponent component;

  @Nested
  class SuccessTests {

    @Test
    @DisplayName("正常系: モックコンポーネントの価格が正しく計算されること")
    void test01() {
      when(component.price()).thenReturn(1500);

      var result = sut.execute(component);

      assertThat(result).isEqualTo(1500);
      verify(component, times(1)).price();
    }

    @Test
    @DisplayName("正常系: 単品商品（Leaf）の価格が取得できること")
    void test02() {
      var item = new ProductItem("キーボード", 8000);

      var result = sut.execute(item);

      assertThat(result).isEqualTo(8000);
    }

    @Test
    @DisplayName("正常系: 階層構造を持つカテゴリー（Composite）全体の再帰集計が正しく行われること")
    void test03() {
      // 親カテゴリー: パソコン周辺機器
      var rootCategory = new ProductCategory("パソコン周辺機器");

      // 子要素: 単品商品
      var mouse = new ProductItem("マウス", 3000);
      rootCategory.add(mouse);

      // 子要素: サブカテゴリー（オーディオ機器）
      var subCategory = new ProductCategory("オーディオ");
      var speaker = new ProductItem("スピーカー", 5000);
      var headphone = new ProductItem("ヘッドホン", 12000);
      subCategory.add(speaker);
      subCategory.add(headphone);

      rootCategory.add(subCategory);

      // 実行: 3000 + 5000 + 12000 = 20000
      var result = sut.execute(rootCategory);

      assertThat(result).isEqualTo(20000);
    }
  }

  @Nested
  class ExceptionTests {

    @Test
    @DisplayName("異常系: コンポーネントがnullの場合、NullPointerExceptionが発生すること")
    void test01() {
      assertThatThrownBy(() -> sut.execute(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("component must not be null");
    }

    @Test
    @DisplayName("異常系: 商品の価格に負の値を設定した場合、IllegalArgumentExceptionが発生すること")
    void test02() {
      assertThatThrownBy(() -> new ProductItem("不正商品", -100))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("price must not be negative");
    }

    @Test
    @DisplayName("異常系: カテゴリーにnull要素を追加した場合、NullPointerExceptionが発生すること")
    void test03() {
      var category = new ProductCategory("家電");

      assertThatThrownBy(() -> category.add(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("component must not be null");
    }
  }
}
