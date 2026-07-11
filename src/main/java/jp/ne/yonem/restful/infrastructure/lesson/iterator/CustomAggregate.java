package jp.ne.yonem.restful.infrastructure.lesson.iterator;

/** 集合体を表すインターフェースです。 */
public interface CustomAggregate {
  CustomIterator iterator();
}
