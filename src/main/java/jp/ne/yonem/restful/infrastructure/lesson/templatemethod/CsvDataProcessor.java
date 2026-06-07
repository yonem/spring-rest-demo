package jp.ne.yonem.restful.infrastructure.lesson.templatemethod;

/** CSVデータを対象とした具体的なデータ処理クラスです。 */
public class CsvDataProcessor extends AbstractDataProcessor {

  @Override
  protected String read(String source) {
    return "CSV_RAW_DATA:" + source;
  }

  @Override
  protected String transform(String rawData) {
    return rawData.toUpperCase();
  }

  @Override
  protected void save(String data) {
    System.out.println("CSVデータを保存しました: " + data);
  }
}
