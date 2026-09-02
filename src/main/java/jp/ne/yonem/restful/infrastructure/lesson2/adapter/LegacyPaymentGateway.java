package jp.ne.yonem.restful.infrastructure.lesson2.adapter;

/** 互換性のない外部レガシー決済システム（Adaptee）です。 */
public class LegacyPaymentGateway {

  /**
   * 旧システムの決済メソッド（引数順やメソッド名が標準と異なる）。
   *
   * @param amount 金額
   * @param userCode ユーザーコード
   * @return 決済結果コード
   */
  public int executeTransaction(int amount, String userCode) {
    // 成功時: 200を返却する仕様
    return 200;
  }
}
