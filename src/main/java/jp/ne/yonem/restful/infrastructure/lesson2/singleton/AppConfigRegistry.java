package jp.ne.yonem.restful.infrastructure.lesson2.singleton;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/** アプリケーション全体で一意となる設定レジストリクラス（Singleton）です。 */
public final class AppConfigRegistry {

  // Initialization-on-demand holder idiom によるスレッドセーフな遅延初期化
  private static class InstanceHolder {
    private static final AppConfigRegistry INSTANCE = new AppConfigRegistry();
  }

  private final Map<String, String> settings = new ConcurrentHashMap<>();

  // 外部からのインスタンス化を完全に禁止
  private AppConfigRegistry() {}

  /**
   * 唯一のインスタンスを取得します。
   *
   * @return AppConfigRegistryのインスタンス
   */
  public static AppConfigRegistry getInstance() {
    return InstanceHolder.INSTANCE;
  }

  /**
   * 設定値を登録します。
   *
   * @param key キー
   * @param value 値
   */
  public void put(String key, String value) {
    var safeKey = Objects.requireNonNull(key, "key must not be null");
    var safeValue = Objects.requireNonNull(value, "value must not be null");
    this.settings.put(safeKey, safeValue);
  }

  /**
   * 設定値を取得します。
   *
   * @param key キー
   * @return 設定値（存在しない場合はnull）
   */
  public String get(String key) {
    var safeKey = Objects.requireNonNull(key, "key must not be null");
    return this.settings.get(safeKey);
  }
}
