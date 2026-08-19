package jp.ne.yonem.restful.infrastructure.lesson2.factorymethod;

/** PDFレポートを生成する具体工場です。 */
public class PdfReportFactory extends AbstractReportFactory {

  @Override
  public ReportProduct createReport() {
    return new PdfReportProduct();
  }
}
