package jp.ne.yonem.restful.infrastructure.llm;

import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.onnx.allminilml6v2q.AllMiniLmL6V2QuantizedEmbeddingModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiStreamingChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/** ドキュメントから知識を構築し、永続化（保存）して再利用するためのクラスです。 */
public class GeminiChatAssistant {

  // ベクトルデータの保存先ファイル名
  private static final String PERSISTENCE_FILE = "knowledge_base.json";

  interface Assistant {
    @SystemMessage(
        """
        あなたは社内ドキュメントの専門家です。
        以下の制約を厳守してください：
        - 質問には必ず日本語で回答すること。
        - 英語で回答してはいけません。
        - 提供された資料の内容に基づいて回答すること。
        """)
    TokenStream chat(String message);
  }

  public void execute(String directoryPath, String question) {

    // 1. Gemini のストリーミングモデルを構築
    var model =
        GoogleAiGeminiStreamingChatModel.builder()
            .apiKey("ここにコピーしたAPIキーを貼り付け")
            .modelName("gemini-3-flash-preview") // 高速で無料枠があるモデル
            .temperature(0.1) // 日本語を安定させるため低めに設定
            .build();

    var embeddingModel = new AllMiniLmL6V2QuantizedEmbeddingModel();
    InMemoryEmbeddingStore<TextSegment> embeddingStore;
    var storePath = Paths.get(PERSISTENCE_FILE);

    // 2. 永続化データの読み込み または 新規構築
    if (Files.exists(storePath)) {

      // 保存済みファイルがある場合は、Embedding計算をスキップして高速ロード
      System.out.println("既存のナレッジファイルをロードしています...");
      embeddingStore = InMemoryEmbeddingStore.fromFile(storePath); //

    } else {
      // ファイルがない場合は、初回のみ重いベクトル化計算を実行
      System.out.println("新しいドキュメントを解析中（初回のみ時間がかかります）...");
      embeddingStore = new InMemoryEmbeddingStore<>();
      var documents = FileSystemDocumentLoader.loadDocumentsRecursively(Paths.get(directoryPath));
      var ingestor =
          EmbeddingStoreIngestor.builder()
              .embeddingModel(embeddingModel)
              .embeddingStore(embeddingStore)
              .build();
      ingestor.ingest(documents);

      // 次回のためにファイルへ書き出し
      embeddingStore.serializeToFile(storePath);
      System.out.println("解析が完了し、ナレッジを保存しました。");
    }

    // 3. RAGエンジンの構築
    var contentRetriever =
        EmbeddingStoreContentRetriever.builder()
            .embeddingStore(embeddingStore)
            .embeddingModel(embeddingModel)
            .maxResults(5)
            .build();

    var assistant =
        AiServices.builder(Assistant.class)
            .streamingChatModel(model)
            .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
            .contentRetriever(contentRetriever)
            .build();

    // 6. 質問の実行とストリーミング処理
    System.out.println("AIに質問中（ストリーミング）...");
    System.out.println("--- AIの回答 ---");

    var tokenStream = assistant.chat(question);
    var future = new CompletableFuture<Void>();

    tokenStream
        .onPartialResponse(System.out::print)
        .onCompleteResponse(
            response -> {
              System.out.println("\n--- 回答完了 ---");
              future.complete(null);
            })
        .onError(
            err -> {
              err.printStackTrace();
              future.completeExceptionally(err);
            })
        .start();

    try {
      future.get();
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }
}
