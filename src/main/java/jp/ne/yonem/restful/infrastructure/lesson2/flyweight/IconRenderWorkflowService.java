package jp.ne.yonem.restful.infrastructure.lesson2.flyweight;

import java.util.Objects;
import org.springframework.stereotype.Service;

/** Flyweightパターンを利用して大量のアイコンを描画管理するサービスです。 */
@Service
public class IconRenderWorkflowService {

  /**
   * 指定されたアイコン種別・座標・色でアイコンを描画します。
   *
   * @param factory アイコン工場
   * @param iconType アイコン種別
   * @param x X座標
   * @param y Y座標
   * @param color 色
   * @return 描画ログ
   */
  public String execute(IconFactory factory, String iconType, int x, int y, String color) {
    var safeFactory = Objects.requireNonNull(factory, "factory must not be null");
    var safeIconType = Objects.requireNonNull(iconType, "iconType must not be null");
    var safeColor = Objects.requireNonNull(color, "color must not be null");

    // 工場から共有インスタンス（Flyweight）を取得
    var icon = safeFactory.getIcon(safeIconType);

    // 外省的状態（座標や色）を渡して処理を実行
    return icon.draw(x, y, safeColor);
  }
}
