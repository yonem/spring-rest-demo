package jp.ne.yonem.restful.infrastructure.lesson2.decorator;

import java.util.Objects;

/** デコレーターの抽象基底クラスです。 */
public abstract class DataWriterDecorator implements DataWriter {

  protected final DataWriter wrappee;

  protected DataWriterDecorator(DataWriter wrappee) {
    this.wrappee = Objects.requireNonNull(wrappee, "wrappee must not be null");
  }

  @Override
  public String writeData(String data) {
    return wrappee.writeData(data);
  }
}
