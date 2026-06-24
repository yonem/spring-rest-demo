package jp.ne.yonem.restful.infrastructure.lesson.mediator;

/** 管制塔（仲介者）の機能を表すインターフェースです。 */
public interface ControlTowerMediator {
  void register(AircraftColleague aircraft);

  void sendMessage(String message, AircraftColleague originator);
}
