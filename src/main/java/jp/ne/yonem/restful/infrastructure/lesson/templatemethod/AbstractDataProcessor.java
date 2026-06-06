package jp.ne.yonem.restful.infrastructure.lesson.templatemethod;

import java.util.Objects;

/** データ処理パイプラインの骨組みを定義する抽象クラスです。 */
public abstract class AbstractDataProcessor {

  /**
   * データの読み込み、加工、保存を一連のアルゴリズムとして実行します（Template Method）。 サブクラスでのオーバーライドを禁止するため final を付与します。
   *
   * @param source 処理対象のデータ元
   * @return 処理結果
   */
  public final String process(String source) {
    var safeSource = Objects.requireNonNull(source, "source must not be null");

    // 固定されたアルゴリズムの手順
    var rawData = read(safeSource);
    var transformedData = transform(rawData);
    save(transformedData);

    return transformedData;
  }

  /** データを読み込みます。サブクラスで具現化します。 */
  protected abstract String read(String source);

  /** データを加工します。サブクラスで具現化します。 */
  protected abstract String transform(String rawData);

  /** データを保存します。サブクラスで具現化します。 */
  protected abstract void save(String data);
}
