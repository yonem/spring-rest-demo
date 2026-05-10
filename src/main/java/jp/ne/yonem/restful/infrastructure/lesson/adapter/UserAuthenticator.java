package jp.ne.yonem.restful.infrastructure.lesson.adapter;

/** 現在のプロジェクトで定義されている、標準的な認証インターフェースです。 */
public interface UserAuthenticator {
  boolean authenticate(String username, String password);
}
