package jp.ne.yonem.restful.infrastructure.lesson.proxy;

/** 重い処理を行う本物のクラスです。 */
class RealDataProvider implements DataProvider {

  public RealDataProvider() {
    // インスタンス化に非常に時間がかかる想定
    System.out.println("RealDataProvider: 重い初期化処理を実行中...");
  }

  @Override
  public String fetchData() {
    return "重要なデータ";
  }
}
