package jp.ne.yonem.restful.infrastructure.lesson;

import java.util.ArrayList;
import java.util.List;

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

  @FunctionalInterface
  public interface StockObserver {
    void onUpdate(int currentStock);
  }

  private final List<StockObserver> observers = new ArrayList<>();

  /** オブザーバーを登録するメソッド */
  public void addObserver(StockObserver observer) {
    observers.add(observer);
  }

  public void reduceStock(int amount) {
    this.stockCount -= amount;
    // 登録されている全員に通知するだけ（誰がいるかは気にしない）
    notifyObservers();
  }

  private void notifyObservers() {
    observers.forEach(observer -> observer.onUpdate(stockCount));
  }

  public static void modern() {
    var manager = new StockManager();

    // 具象クラスを作らずに、ラムダ式でサクッと購読者を追加できる
    manager.addObserver(stock -> System.out.println("[Log] 現在の残数: " + stock));
    manager.addObserver(stock -> System.out.println("[Email] 在庫通知メールを送信しました: " + stock));

    // 後から「Slack通知」が必要になっても、StockManagerを弄らずに追加可能
    manager.addObserver(
        stock -> {
          if (stock < 20) System.out.println("[Slack] 警告！在庫が少なすぎます！");
        });

    System.out.println("--- 在庫を減らします ---");
    manager.reduceStock(30);

    System.out.println("\n--- さらに在庫を減らします ---");
    manager.reduceStock(60);
  }
}
