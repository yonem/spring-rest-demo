package jp.ne.yonem.restful.infrastructure.lesson2.command;

import org.springframework.stereotype.Component;

/** 実際に家電を操作する受信者（Receiver）です。 */
@Component
public class LightReceiver {

  public String turnOn() {
    return "Light is ON";
  }

  public String turnOff() {
    return "Light is OFF";
  }
}
