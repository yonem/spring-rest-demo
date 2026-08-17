package jp.ne.yonem.restful.infrastructure.lesson2.factorymethod;

/** PDF形式のレポートを表す具体クラスです。 */
public class PdfReportProduct implements ReportProduct {

  @Override
  public String export(String title, String content) {
    return "[PDF] Title: %s, Content: %s".formatted(title, content);
  }
}
