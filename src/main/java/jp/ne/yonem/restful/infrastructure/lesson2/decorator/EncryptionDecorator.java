package jp.ne.yonem.restful.infrastructure.lesson2.decorator;

import java.util.Base64;
import java.util.Objects;

/** データをBase64で暗号化（エンコード）するデコレーターです。 */
public class EncryptionDecorator extends DataWriterDecorator {

  public EncryptionDecorator(DataWriter wrappee) {
    super(wrappee);
  }

  @Override
  public String writeData(String data) {
    var safeData = Objects.requireNonNull(data, "data must not be null");
    var encrypted = Base64.getEncoder().encodeToString(safeData.getBytes());
    return super.writeData(encrypted);
  }
}
