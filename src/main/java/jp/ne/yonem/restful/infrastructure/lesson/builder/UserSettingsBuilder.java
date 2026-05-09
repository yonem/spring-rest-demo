package jp.ne.yonem.restful.infrastructure.lesson.builder;

import java.util.Objects;

/** UserSettingsを段階的に構築するBuilderクラスです。 */
public class UserSettingsBuilder {
  private String userId;
  private String theme = "LIGHT";
  private boolean notificationsEnabled = true;
  private int retryCount = 3;

  public UserSettingsBuilder userId(String userId) {
    this.userId = Objects.requireNonNull(userId, "userId must not be null");
    return this;
  }

  public UserSettingsBuilder theme(String theme) {
    this.theme = Objects.requireNonNull(theme, "theme must not be null");
    return this;
  }

  public UserSettingsBuilder notificationsEnabled(boolean enabled) {
    this.notificationsEnabled = enabled;
    return this;
  }

  /**
   * インスタンスを生成します。
   *
   * @return UserSettings
   */
  public UserSettings build() {
    Objects.requireNonNull(userId, "userId is required for building");
    return new UserSettings(userId, theme, notificationsEnabled, retryCount);
  }
}
