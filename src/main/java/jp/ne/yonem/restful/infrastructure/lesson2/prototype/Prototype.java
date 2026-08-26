package jp.ne.yonem.restful.infrastructure.lesson2.prototype;

/**
 * 自身を複製可能なオブジェクトを表すプロトタイプインターフェースです。
 *
 * @param <T> 複製対象の型
 */
public interface Prototype<T> {
  T clone();
}
