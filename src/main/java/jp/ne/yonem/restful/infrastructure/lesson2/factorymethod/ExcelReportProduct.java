package jp.ne.yonem.restful.infrastructure.lesson2.factorymethod;

/** Excel形式のレポートを表す具体クラスです。 */
public class ExcelReportProduct implements ReportProduct {

  @Override
  public String export(String title, String content) {
    return "[Excel] Sheet: %s, Data: %s".formatted(title, content);
  }
}
