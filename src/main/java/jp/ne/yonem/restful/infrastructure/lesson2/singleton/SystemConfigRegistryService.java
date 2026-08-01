package jp.ne.yonem.restful.infrastructure.lesson2.singleton;

import java.util.Objects;
import org.springframework.stereotype.Service;

/** シングルトンレジストリを操作するサービスです。 */
@Service
public class SystemConfigRegistryService {

  /**
   * 指定した設定項目をシングルトンレジストリへ保存・参照します。
   *
   * @param key 設定キー
   * @param value 設定値
   * @return レジストリから読み直した値
   */
  public String registerAndGet(String key, String value) {
    var safeKey = Objects.requireNonNull(key, "key must not be null");
    var safeValue = Objects.requireNonNull(value, "value must not be null");

    // シングルトンインスタンスの取得
    var registry = AppConfigRegistry.getInstance();
    registry.put(safeKey, safeValue);

    return registry.get(safeKey);
  }
}
