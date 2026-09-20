package jp.ne.yonem.restful.infrastructure.lesson2.decorator;

/** データ書き込み（出力）の共通インターフェースです。 */
public interface DataWriter {
  String writeData(String data);
}
