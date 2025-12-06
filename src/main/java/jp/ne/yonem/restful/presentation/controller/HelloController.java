package jp.ne.yonem.restful.presentation.controller;

import jp.ne.yonem.restful.presentation.dto.MessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

// このアノテーションでRESTfulなコントローラーであることを示す
@RestController
@RequestMapping("/api/greeting") // 全てのエンドポイントのパスの前に /api をつける
@Slf4j
public class HelloController {

  // GETリクエストの受け取りとレスポンス送信
  // 例: GET /api/hello?name=World
  @GetMapping("/hello")
  public String getHelloMessage(@RequestParam(value = "name", defaultValue = "Guest") String name) {
    // リクエストパラメータ 'name' を受け取り、文字列を返す
    log.info("getHelloMessage");
    return "Hello, " + name + "!";
  }

  // POSTリクエストの受け取りとレスポンス送信
  // リクエストボディにJSONデータを受け取る例
  // 例: POST /api/message, Body: {"content": "Hello from client!"}
  @PostMapping("/message")
  public String postMessage(@RequestBody MessageRequest messageRequest) {
    // @RequestBody でリクエストボディのJSONをJavaオブジェクトにマッピング
    // MessageRequest オブジェクトの content フィールドを取得
    return "Received your message: '" + messageRequest.getContent() + "'. Server says hi!";
  }
}
