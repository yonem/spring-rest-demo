package jp.ne.yonem.restful.infrastructure.lesson2.decorator;

import java.util.Objects;

/** データにプレフィックス（[COMPRESSED]）を付与して圧縮を模倣するデコレーターです。 */
public class CompressionDecorator extends DataWriterDecorator {

  public CompressionDecorator(DataWriter wrappee) {
    super(wrappee);
  }

  @Override
  public String writeData(String data) {
    var safeData = Objects.requireNonNull(data, "data must not be null");
    var compressed = "[COMPRESSED]" + safeData;
    return super.writeData(compressed);
  }
}
