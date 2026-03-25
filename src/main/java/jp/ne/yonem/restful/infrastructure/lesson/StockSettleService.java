package jp.ne.yonem.restful.infrastructure.lesson;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** 在庫の減算および購読者への通知を制御するサービスです。 */
public class StockSettleService {

  /** 在庫更新イベントのデータを保持するレコードです。 */
  public record StockEvent(String productId, int currentStock, LocalDateTime updatedAt) {}

  /** 在庫更新を購読するオブザーバーのインターフェースです。 */
  @FunctionalInterface
  public interface StockObserver {
    void onUpdate(StockEvent event);
  }

  private final List<StockObserver> observers = new ArrayList<>();
  private int stockCount = 100;

  /**
   * 在庫を減算し、登録されたすべてのオブザーバーに通知を実行します。
   *
   * @param amount 減算する数量
   */
  public void execute(int amount) {
    var previousStock = stockCount;
    stockCount = Math.max(0, previousStock - amount);

    var event = new StockEvent("PROD-001", stockCount, LocalDateTime.now());
    notifyObservers(event);
  }

  public void addObserver(StockObserver observer) {
    Optional.ofNullable(observer).ifPresent(observers::add);
  }

  private void notifyObservers(StockEvent event) {
    observers.forEach(observer -> observer.onUpdate(event));
  }
}
