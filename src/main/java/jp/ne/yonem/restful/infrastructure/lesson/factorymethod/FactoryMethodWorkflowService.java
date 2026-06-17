package jp.ne.yonem.restful.infrastructure.lesson.factorymethod;

import java.util.Objects;
import org.springframework.stereotype.Service;

/** ファクトリメソッドを検証・実行するサービスです。 */
@Service
public class FactoryMethodWorkflowService {

  /**
   * 指定された工場からメッセージを生成して処理を実行します。
   *
   * @param factory メッセージ工場
   * @return 生成されたメッセージ結果
   */
  public String execute(AbstractMessageFactory factory) {
    var safeFactory = Objects.requireNonNull(factory, "factory must not be null");
    return safeFactory.prepareMessage();
  }
}
