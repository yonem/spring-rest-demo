package jp.ne.yonem.restful.infrastructure.lesson2.bridge;

import java.util.Objects;

/** 機能階層の抽象クラス（Abstraction）です。 */
public abstract class NotificationAbstraction {

  protected final MessageSenderImplementor implementor;

  protected NotificationAbstraction(MessageSenderImplementor implementor) {
    this.implementor = Objects.requireNonNull(implementor, "implementor must not be null");
  }

  public abstract String notify(String title, String body);
}
