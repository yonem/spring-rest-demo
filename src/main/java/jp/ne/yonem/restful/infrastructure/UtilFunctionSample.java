package jp.ne.yonem.restful.infrastructure;

import java.util.UUID;
import java.util.function.*;

@SuppressWarnings("ClassEscapesDefinedScope")
public class UtilFunctionSample {

  // サンプル用のデータモデル
  record User(String id, String name, int age, boolean active) {}

  /** 1. Predicate<T>: 条件判定 (T -> boolean) 意図: 「そのデータは条件を満たしているか？」というチェックに使用します。 */
  public boolean checkUserServiceable(User user) {
    // 「有効かつ18歳以上か？」という判定ルールを定義
    Predicate<User> isAdult = u -> u.age() >= 18;
    Predicate<User> isActive = User::active;

    // 複数の条件を組み合わせることも可能
    return isAdult.and(isActive).test(user);
  }

  /** 2. Function<T, R>: 変換 (T -> R) 意図: 「ある型から別の型へデータを加工・変換する」場合に使用します。 */
  public String getUserSummary(User user) {
    // Userオブジェクトから表示用の文字列へ変換するロジック
    Function<User, String> summaryFormatter =
        u -> "ID: %s, Name: %s (Age: %d)".formatted(u.id(), u.name(), u.age());

    return summaryFormatter.apply(user);
  }

  /** 3. Consumer<T>: 消費・アクション (T -> void) 意図: 「データを受け取って何か（ログ出力、DB保存など）をする」場合に使用します。 */
  public void processUserData(User user) {
    // 外部への通知やログ出力をシミュレート
    Consumer<User> logInfo = u -> System.out.println("[INFO] Processing user: " + u.name());
    Consumer<User> saveToDb = u -> System.out.println("[DB] Saving user to database...");

    // 連続して実行(andThen)することも可能
    logInfo.andThen(saveToDb).accept(user);
  }

  /** 4. Supplier<T>: 生成・提供 (なし -> T) 意図: 「新しいインスタンスを生成する」「必要な時に値を提供する」場合に使用します。 */
  public User createNewUser(String name, int age) {
    // IDを自動採番して新しいUserを作る「工場」のような役割
    Supplier<String> idGenerator = () -> UUID.randomUUID().toString();

    return new User(idGenerator.get(), name, age, true);
  }

  /** 5. BiFunction<T, U, R>: 2引数の変換 (T, U -> R) 意図: 「2つの入力から1つの結果を導き出す」場合に使用します。 */
  public int calculateScore(User user, int bonusPoints) {
    // 年齢とボーナスポイントを合算する計算ロジック
    BiFunction<User, Integer, Integer> scoreCalc = (u, bonus) -> (u.age() * 10) + bonus;

    return scoreCalc.apply(user, bonusPoints);
  }
}
