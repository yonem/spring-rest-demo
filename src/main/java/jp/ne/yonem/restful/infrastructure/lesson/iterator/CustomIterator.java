package jp.ne.yonem.restful.infrastructure.lesson.iterator;

/** 要素を順番に走査するためのインターフェースです。 */
public interface CustomIterator {
  boolean hasNext();

  Object next();
}
