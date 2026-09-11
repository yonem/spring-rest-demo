package jp.ne.yonem.restful.infrastructure.lesson2.bridge;

import java.util.Objects;

/** 通常の通知機能（Refined Abstraction） */
public class NormalNotification extends NotificationAbstraction {

  public NormalNotification(MessageSenderImplementor implementor) {
    super(implementor);
  }

  @Override
  public String notify(String title, String body) {
    var safeTitle = Objects.requireNonNull(title, "title must not be null");
    var safeBody = Objects.requireNonNull(body, "body must not be null");
    return implementor.sendRawMessage(safeTitle, safeBody);
  }
}
