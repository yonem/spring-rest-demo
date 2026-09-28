package jp.ne.yonem.restful.infrastructure.lesson2.decorator;

import java.util.Objects;

/** 基本的なプレーンテキスト書き込みを行うクラスです。 */
public class PlainDataWriter implements DataWriter {

  @Override
  public String writeData(String data) {
    return Objects.requireNonNull(data, "data must not be null");
  }
}
