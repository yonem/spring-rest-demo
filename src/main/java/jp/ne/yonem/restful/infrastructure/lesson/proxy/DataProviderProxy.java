package jp.ne.yonem.restful.infrastructure.lesson.proxy;

import java.util.Optional;

/** RealDataProviderの身代わりとなるプロキシクラスです。 */
public class DataProviderProxy implements DataProvider {

  private RealDataProvider realProvider;

  @Override
  public String fetchData() {
    System.out.println("Proxy: アクセスログを記録します。");

    // 必要になったタイミングで初めて本物を生成する（遅延初期化）
    this.realProvider = Optional.ofNullable(this.realProvider).orElseGet(RealDataProvider::new);

    var data = this.realProvider.fetchData();

    System.out.println("Proxy: 処理が完了しました。");
    return data;
  }
}
