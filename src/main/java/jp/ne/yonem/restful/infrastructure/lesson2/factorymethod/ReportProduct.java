package jp.ne.yonem.restful.infrastructure.lesson2.factorymethod;

/** 出力するレポート（製品）の共通インターフェースです。 */
public interface ReportProduct {
  String export(String title, String content);
}
