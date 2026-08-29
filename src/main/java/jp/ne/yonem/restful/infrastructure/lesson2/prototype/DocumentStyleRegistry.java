package jp.ne.yonem.restful.infrastructure.lesson2.prototype;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/** 事前定義されたプロトタイプインスタンスを管理するレジストリクラスです。 */
public class DocumentStyleRegistry {

  private final Map<String, DocumentStylePrototype> prototypes = new ConcurrentHashMap<>();

  public DocumentStyleRegistry() {

    // デフォルトの雛形を事前登録
    prototypes.put("DEFAULT", new DocumentStylePrototype("Arial", 12, "#000000"));
    prototypes.put("DARK_MODE", new DocumentStylePrototype("Consolas", 14, "#FFFFFF"));
  }

  /**
   * 指定したキーのプロトタイプを複製して取得します。
   *
   * @param key 雛形キー
   * @return 複製された新しい DocumentStylePrototype
   */
  public DocumentStylePrototype getCloned(String key) {
    var safeKey = Objects.requireNonNull(key, "key must not be null");
    var prototype = prototypes.get(safeKey);

    if (prototype == null) {
      throw new IllegalArgumentException("Prototype not found for key: " + safeKey);
    }
    return prototype.clone();
  }
}
