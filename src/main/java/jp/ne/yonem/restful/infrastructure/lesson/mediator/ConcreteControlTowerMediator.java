package jp.ne.yonem.restful.infrastructure.lesson.mediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** 航空管制を統括する具体的な管制塔クラスです。 */
public class ConcreteControlTowerMediator implements ControlTowerMediator {

  private final List<AircraftColleague> aircraftList = new ArrayList<>();

  @Override
  public void register(AircraftColleague aircraft) {
    var safeAircraft = Objects.requireNonNull(aircraft, "aircraft must not be null");
    this.aircraftList.add(safeAircraft);
  }

  @Override
  public void sendMessage(String message, AircraftColleague originator) {
    var safeMessage = Objects.requireNonNull(message, "message must not be null");
    var safeOriginator = Objects.requireNonNull(originator, "originator must not be null");

    // 発信元「以外」のすべての飛行機にメッセージを仲介・転送する
    this.aircraftList.stream()
        .filter(aircraft -> !Objects.equals(aircraft.getCallSign(), safeOriginator.getCallSign()))
        .forEach(aircraft -> aircraft.receive(safeMessage));
  }
}
