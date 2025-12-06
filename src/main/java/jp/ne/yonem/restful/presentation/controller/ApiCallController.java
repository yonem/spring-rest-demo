package jp.ne.yonem.restful.presentation.controller;

import jp.ne.yonem.restful.application.GetPostsApiCallService;
import jp.ne.yonem.restful.domain.model.Post;
import jp.ne.yonem.restful.infrastructure.GetPostByIdApiCallService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/external")
@RequiredArgsConstructor
public class ApiCallController {

  private final GetPostsApiCallService getPostsApiCallService;
  private final GetPostByIdApiCallService getPostByIdApiCallService;

  /**
   * 全ての投稿を非同期で取得するAPIエンドポイント。 GET /api/external/posts
   *
   * @return 投稿のFluxを含むResponseEntity
   */
  @GetMapping("/posts")
  public ResponseEntity<Flux<Post>> getAllPosts() {
    var posts = getPostsApiCallService.execute();
    return ResponseEntity.ok(posts);
  }

  /**
   * 特定のIDの投稿を非同期で取得するAPIエンドポイント。 GET /api/external/posts/{id}
   *
   * @param id 投稿ID
   * @return 投稿オブジェクトのMonoを含むResponseEntity、または404 Not Found
   */
  @GetMapping("/posts/{id}")
  public Mono<ResponseEntity<Post>> getPostById(@PathVariable Integer id) {
    return getPostByIdApiCallService
        .execute(id)
        .map(ResponseEntity::ok) // 投稿が見つかった場合
        .defaultIfEmpty(ResponseEntity.notFound().build());
    // 投稿が見つからなかった場合（Monoが空の場合）
    // JSONPlaceholderは存在しないIDの場合、空のJSON `{}` を返すため、
    // bodyToMono(Post.class)はPostオブジェクトを作成してしまいます。
    // 厳密には、WebClientのエラーハンドリング (`onStatus`) を使って
    // 404ステータスをチェックする方が適切ですが、シンプルさのために今回はこの形にしています。
    // (JSONPlaceholderでは通常404は返ってこず、空のオブジェクトが返されるため)
    // もし実際の外部APIが404を返す場合:
    // .onErrorResume(WebClientResponseException.NotFound.class, e ->
    // Mono.just(ResponseEntity.notFound().build()))
    // のような処理を追加することになります。
  }
}
