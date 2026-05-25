package jp.ne.yonem.restful.infrastructure.lesson.proxy;

import java.util.Objects;
import org.springframework.stereotype.Service;

/** プロキシを利用してデータを取得するサービスです。 */
@Service
public class DataFetchService {

  /**
   * データを取得して返します。
   *
   * @param provider データプロバイダー
   * @return 取得データ
   */
  public String execute(DataProvider provider) {
    var safeProvider = Objects.requireNonNull(provider, "provider must not be null");
    return safeProvider.fetchData();
  }
}
