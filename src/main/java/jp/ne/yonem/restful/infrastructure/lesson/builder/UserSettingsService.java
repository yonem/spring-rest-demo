package jp.ne.yonem.restful.infrastructure.lesson.builder;

import java.util.Objects;
import org.springframework.stereotype.Service;

/** Builderパターンを使用してユーザー設定を構築するサービスです。 */
@Service
public class UserSettingsService {

  /**
   * ユーザーIDに基づいてカスタマイズされた設定を生成します。
   *
   * @param userId ユーザーID
   * @return 構築された設定情報
   */
  public UserSettings execute(String userId) {
    var builder = UserSettings.builder().userId(userId);

    // 特定のユーザー（ADMINなど）には特別な設定を適用する例
    if (Objects.nonNull(userId) && userId.startsWith("ADM")) {
      builder.theme("DARK").disableNotifications();
    }
    return builder.build();
  }
}
