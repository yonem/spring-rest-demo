package jp.ne.yonem.restful.application;

import jp.ne.yonem.restful.domain.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class GetPostsApiCallService {

  private static final String API_URL = "https://jsonplaceholder.typicode.com"; // ベースURLを設定

  private final WebClient webClient;

  /**
   * JSONPlaceholderから投稿一覧を非同期で取得します。
   *
   * @return 投稿のFlux (非同期ストリーム)
   */
  public Flux<Post> execute() {
    return webClient
        .get()
        .uri(API_URL + "/posts") // エンドポイント
        .retrieve() // レスポンスボディの取得を開始
        .bodyToFlux(Post.class); // レスポンスボディをPostオブジェクトのFluxとして変換
  }
}
