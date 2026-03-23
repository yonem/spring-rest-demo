package jp.ne.yonem.restful.infrastructure.lesson;

/** ObserverパターンのLesson */
public class StockManager {
  private int stockCount = 100;

  public void legacy(int amount) {
    this.stockCount -= amount;

    // 新人がやりがちな実装：通知したい対象を直接呼び出す
    // 新しい通知先（LINE通知など）が増えるたびに、このメソッドを書き換える必要がある
    System.out.println("[Log] 在庫が減りました。現在の残数: " + stockCount);
    sendEmailExample("在庫通知: 残りは " + stockCount + " 個です。");
  }

  private void sendEmailExample(String message) {
    System.out.println("[Email] " + message);
  }
}
