package jp.ne.yonem.restful.application;

import jp.ne.yonem.restful.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GetPostByIdApiCallService {

  private static final String API_URL = "https://jsonplaceholder.typicode.com"; // ベースURLを設定

  private final WebClient webClient;

  /**
   * 指定されたIDの投稿をJSONPlaceholderから非同期で取得します。
   *
   * @param id 投稿ID
   * @return 投稿オブジェクトのMono (非同期ストリーム、単一の要素)
   */
  public Mono<Post> execute(Integer id) {
    return webClient
        .get()
        .uri(API_URL + "/posts/{id}", id) // パス変数を含むURI
        .retrieve()
        .bodyToMono(Post.class); // レスポンスボディをPostオブジェクトのMonoとして変換
  }
}
