package jp.ne.yonem.restful.infrastructure.lesson2.factorymethod;

/** Excelレポートを生成する具体工場です。 */
public class ExcelReportFactory extends AbstractReportFactory {

  @Override
  public ReportProduct createReport() {
    return new ExcelReportProduct();
  }
}
