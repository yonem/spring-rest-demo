package jp.ne.yonem.restful.infrastructure.lesson.iterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

/** イテレータパターンによる要素走査を統括するサービスです。 */
@Service
public class IteratorAggregationService {

  /**
   * 指定された集合体から要素をすべて取り出し、タイトルリストを構築します。
   *
   * @param aggregate 走査対象の集合体
   * @return タイトルのリスト
   */
  public List<String> execute(CustomAggregate aggregate) {
    var safeAggregate = Objects.requireNonNull(aggregate, "aggregate must not be null");
    var titles = new ArrayList<String>();

    // 内部表現を隠蔽したまま走査
    var iterator = safeAggregate.iterator();

    while (iterator.hasNext()) {
      var element = iterator.next();

      if (element instanceof LessonBook(String title)) {
        titles.add(title);
      }
    }
    return titles;
  }
}
