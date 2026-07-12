package jp.ne.yonem.restful.infrastructure.lesson.iterator;

import java.util.Objects;

/** 本棚に格納される書籍を表すRecordです。 */
public record LessonBook(String title) {
  public LessonBook {
    Objects.requireNonNull(title, "title must not be null");
  }
}
