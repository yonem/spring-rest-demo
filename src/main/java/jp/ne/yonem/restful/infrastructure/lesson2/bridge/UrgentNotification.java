package jp.ne.yonem.restful.infrastructure.lesson2.bridge;

import java.util.Objects;

/** 緊急の通知機能（装飾などの機能拡張を行っている） */
public class UrgentNotification extends NotificationAbstraction {

  public UrgentNotification(MessageSenderImplementor implementor) {
    super(implementor);
  }

  @Override
  public String notify(String title, String body) {
    var safeTitle = Objects.requireNonNull(title, "title must not be null");
    var safeBody = Objects.requireNonNull(body, "body must not be null");

    // 機能階層側で「[URGENT]」のプレフィックスを付与する拡張
    return implementor.sendRawMessage("[URGENT] " + safeTitle, safeBody);
  }
}
