package jp.ne.yonem.restful.infrastructure.lesson.iterator;

import java.util.Objects;

/** 本棚を走査する具体的な反復子クラスです。 */
public class BookShelfIterator implements CustomIterator {

  private final BookShelfAggregate bookShelf;
  private int index = 0;

  public BookShelfIterator(BookShelfAggregate bookShelf) {
    this.bookShelf = Objects.requireNonNull(bookShelf, "bookShelf must not be null");
  }

  @Override
  public boolean hasNext() {
    return this.index < this.bookShelf.getLength();
  }

  @Override
  public Object next() {
    // 状態を進めつつ対象データを取得
    var book = this.bookShelf.getBookAt(this.index);
    this.index++;
    return book;
  }
}
