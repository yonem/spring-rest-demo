package jp.ne.yonem.restful.infrastructure;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class UtilFunctionSampleTest {
  private UtilFunctionSample sample;

  @BeforeEach
  void setUp() {
    sample = new UtilFunctionSample();
  }

  @Nested
  @DisplayName("Predicate: checkUserServiceable のテスト")
  class PredicateTests {
    @ParameterizedTest(name = "年齢{0}, アクティブ状態{1} のとき結果は{2}")
    @CsvSource({
      "18, true,  true", // 境界値：18歳以上かつアクティブ
      "17, true,  false", // 境界値：18歳未満
      "20, false, false" // 非アクティブ
    })
    void testCheckUserServiceable(int age, boolean active, boolean expected) {
      var user = new UtilFunctionSample.User("1", "Test", age, active);
      var result = sample.checkUserServiceable(user);
      assertThat(result).isEqualTo(expected);
    }
  }

  @Nested
  @DisplayName("Function: getUserSummary のテスト")
  class FunctionTests {
    @Test
    @DisplayName("ユーザー情報が正しく文字列に変換されること")
    void testGetUserSummary() {
      var user = new UtilFunctionSample.User("123", "田中太郎", 25, true);
      var result = sample.getUserSummary(user);

      assertThat(result).contains("ID: 123", "Name: 田中太郎", "Age: 25");
    }
  }

  @Nested
  @DisplayName("Consumer: processUserData のテスト")
  class ConsumerTests {
    @Test
    @DisplayName("例外が発生せずに処理が完了すること（副作用の検証）")
    void testProcessUserData() {
      var user = new UtilFunctionSample.User("1", "田中", 30, true);
      // Consumerは戻り値がないため、実行して例外が起きないことを確認
      assertDoesNotThrow(() -> sample.processUserData(user));
    }
  }

  @Nested
  @DisplayName("Supplier: createNewUser のテスト")
  class SupplierTests {
    @Test
    @DisplayName("新しいユーザーが生成され、IDがUUID形式であること")
    void testCreateNewUser() {
      var name = "佐藤";
      var age = 20;
      var user = sample.createNewUser(name, age);

      assertThat(user).isNotNull();
      assertThat(user.name()).isEqualTo(name);
      assertThat(user.age()).isEqualTo(age);
      // UUIDの形式（8-4-4-4-12桁）に一致するかチェック
      assertThat(user.id())
          .matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
    }
  }

  @Nested
  @DisplayName("BiFunction: calculateScore のテスト")
  class BiFunctionTests {
    @ParameterizedTest(name = "年齢{0}歳, ボーナス{1}点のときスコアは{2}")
    @CsvSource({
      "20, 50,  250", // (20 * 10) + 50 = 250
      "30,  0,  300", // (30 * 10) + 0 = 300
      "0,  10,   10" // (0 * 10) + 10 = 10
    })
    void testCalculateScore(int age, int bonus, int expected) {
      var user = new UtilFunctionSample.User("1", "スコア用", age, true);
      var result = sample.calculateScore(user, bonus);
      assertThat(result).isEqualTo(expected);
    }
  }
}
