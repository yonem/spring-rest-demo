package jp.ne.yonem.restful.infrastructure.lesson.iterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** 書籍の集合体を表す具体的な本棚クラスです。 */
public class BookShelfAggregate implements CustomAggregate {

  private final List<LessonBook> books = new ArrayList<>();

  public void appendBook(LessonBook book) {
    var safeBook = Objects.requireNonNull(book, "book must not be null");
    this.books.add(safeBook);
  }

  public LessonBook getBookAt(int index) {
    return this.books.get(index);
  }

  public int getLength() {
    return this.books.size();
  }

  @Override
  public CustomIterator iterator() {
    return new BookShelfIterator(this);
  }
}
