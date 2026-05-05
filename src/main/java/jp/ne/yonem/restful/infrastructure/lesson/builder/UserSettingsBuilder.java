package jp.ne.yonem.restful.infrastructure.lesson.builder;

import java.util.Objects;

/** UserSettingsを段階的に構築するBuilderクラスです。 */
public class UserSettingsBuilder {
  private String userId;
  private String theme = "LIGHT"; // デフォルト値
  private boolean notificationsEnabled = true;

  public UserSettingsBuilder userId(String userId) {
    this.userId = userId;
    return this;
  }

  public UserSettingsBuilder theme(String theme) {
    this.theme = theme;
    return this;
  }

  public UserSettingsBuilder disableNotifications() {
    this.notificationsEnabled = false;
    return this;
  }

  /**
   * 設定情報を確定させ、UserSettingsインスタンスを生成します。
   *
   * @return 構築されたUserSettings
   */
  public UserSettings build() {
    // 必須チェックなどはここで行う
    var id = Objects.requireNonNull(userId, "userIdは必須です");
    var retryCount = 3;
    return new UserSettings(id, theme, notificationsEnabled, retryCount);
  }
}
