package jp.ne.yonem.restful.infrastructure.lesson2.factorymethod;

/** レポート生成を定義する抽象工場クラス（Creator）です。 */
public abstract class AbstractReportFactory {

  /**
   * Factory Method: どの製品インスタンスを作るかはサブクラスに委ねます。
   *
   * @return レポート製品
   */
  public abstract ReportProduct createReport();

  /** レポートを生成して処理を実行する共通処理です。 */
  public String generate(String title, String content) {
    var product = createReport();
    return product.export(title, content);
  }
}
