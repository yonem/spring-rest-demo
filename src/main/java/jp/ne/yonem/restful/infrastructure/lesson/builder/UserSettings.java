package jp.ne.yonem.restful.infrastructure.lesson.builder;

/** 構築対象となるユーザー設定情報のRecordです。 */
public record UserSettings(
    String userId, String theme, boolean notificationsEnabled, int retryCount) {

  /**
   * Builderインスタンスを生成します。
   *
   * @return 新しいBuilder
   */
  public static UserSettingsBuilder builder() {
    return new UserSettingsBuilder();
  }
}
