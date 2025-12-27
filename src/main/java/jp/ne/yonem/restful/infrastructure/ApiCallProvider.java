package jp.ne.yonem.restful.infrastructure;

import jp.ne.yonem.restful.domain.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ApiCallProvider {

  private static final String API_URL = "https://jsonplaceholder.typicode.com"; // ベースURLを設定

  private final WebClient webClient;

  /**
   * 指定されたIDの投稿をJSONPlaceholderから非同期で取得します。
   *
   * @param id 投稿ID
   * @return 投稿オブジェクトのMono (非同期ストリーム、単一の要素)
   */
  public Mono<Post> findPostBy(Integer id) {
    return webClient
        .get()
        .uri(API_URL + "/posts/{id}", id) // パス変数を含むURI
        .retrieve()
        .bodyToMono(Post.class); // レスポンスボディをPostオブジェクトのMonoとして変換
  }
}
