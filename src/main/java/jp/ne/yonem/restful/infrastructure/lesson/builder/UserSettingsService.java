package jp.ne.yonem.restful.infrastructure.lesson.builder;

import java.util.Objects;
import org.springframework.stereotype.Service;

/** Builderを使用して特定の構築レシピを実行するサービスです。 */
@Service
public class UserSettingsService {

  /**
   * ユーザー種別に応じて最適な設定を構築します。
   *
   * @param userId ユーザーID
   * @return 構築された設定
   */
  public UserSettings execute(String userId) {
    var safeId = Objects.requireNonNull(userId, "Input userId must not be null");
    var builder = UserSettings.builder().userId(safeId);

    return switch (safeId) {
      case String id when id.startsWith("ADM") ->
          builder.theme("DARK").notificationsEnabled(false).build();
      case String id when id.startsWith("GUEST") ->
          builder.theme("CLASSIC").notificationsEnabled(true).build();
      default -> builder.build();
    };
  }
}
