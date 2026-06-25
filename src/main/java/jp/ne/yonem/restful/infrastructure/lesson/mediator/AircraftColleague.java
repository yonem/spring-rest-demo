package jp.ne.yonem.restful.infrastructure.lesson.mediator;

import java.util.Objects;

/** 飛行機（参加者）の機能を表す抽象クラスです。 */
public abstract class AircraftColleague {
  protected final ControlTowerMediator mediator;
  protected final String callSign;

  protected AircraftColleague(ControlTowerMediator mediator, String callSign) {
    this.mediator = Objects.requireNonNull(mediator, "mediator must not be null");
    this.callSign = Objects.requireNonNull(callSign, "callSign must not be null");
  }

  public abstract void receive(String message);

  public final String getCallSign() {
    return this.callSign;
  }
}
