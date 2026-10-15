package jp.ne.yonem.restful.infrastructure.lesson2.flyweight;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/** Flyweightオブジェクトを管理・再利用（キャッシュ）する工場クラスです。 */
public class IconFactory {

  private final Map<String, IconFlyweight> cache = new ConcurrentHashMap<>();

  /** キャッシュから既存のアイコンを取得するか、存在しない場合は新規生成して再利用します。 */
  public IconFlyweight getIcon(String iconType) {
    var safeIconType = Objects.requireNonNull(iconType, "iconType must not be null");

    return cache.computeIfAbsent(
        safeIconType,
        type -> {
          // 疑似的に重い画像データをロードして生成
          byte[] dummyImageData = ("ImageDataFor_" + type).getBytes();
          return new IconFlyweight(type, dummyImageData);
        });
  }

  public int getCacheSize() {
    return cache.size();
  }
}
