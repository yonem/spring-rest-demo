package jp.ne.yonem.restful.infrastructure.lesson2.flyweight;

import java.util.Objects;

/**
 * 共有されるアイコンオブジェクト（Flyweight）です。
 *
 * @param iconType Intrinsic State（内省的状態: 共有可能で不変なデータ）
 */
public record IconFlyweight(String iconType, byte[] imageData) {

  public IconFlyweight(String iconType, byte[] imageData) {
    this.iconType = Objects.requireNonNull(iconType, "iconType must not be null");
    this.imageData = Objects.requireNonNull(imageData, "imageData must not be null");
  }

  /** 外省的状態（Extrinsic State: 座標や色）を受け取って描画表現を生成します。 */
  public String draw(int x, int y, String color) {
    var safeColor = Objects.requireNonNull(color, "color must not be null");
    return "Icon[%s] at (%d, %d) with color[%s]".formatted(this.iconType, x, y, safeColor);
  }
}
