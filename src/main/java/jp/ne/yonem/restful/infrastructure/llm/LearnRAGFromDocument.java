package jp.ne.yonem.restful.infrastructure.llm;

import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.onnx.allminilml6v2q.AllMiniLmL6V2QuantizedEmbeddingModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class LearnRAGFromDocument {

  interface Assistant {
    @SystemMessage("あなたは社内ドキュメントに精通したアシスタントです。提供された資料に基づいて、必ず日本語で回答してください。")
    TokenStream chat(String message);
  }

  public void execute(String directoryPath, String question)
      throws ExecutionException, InterruptedException {

    // 1. ローカルLLMの設定
    var model =
        OllamaStreamingChatModel.builder()
            .baseUrl("http://localhost:11434")
            .modelName("llama3")
            .timeout(Duration.ofMinutes(5))
            .build();

    // 2. ドキュメントの読み込み（フォルダ内の全ファイルを対象にする）
    var documents = FileSystemDocumentLoader.loadDocumentsRecursively(Paths.get(directoryPath));

    // 3. ベクトルDB（メモリ型）と埋め込みモデルの準備
    var embeddingStore = new InMemoryEmbeddingStore<TextSegment>();
    var embeddingModel = new AllMiniLmL6V2QuantizedEmbeddingModel();

    // 4. ドキュメントを「取り込む（Ingest）」
    // 文書を適切な長さに分割し、ベクトル化してDBに保存する工程を一括で行います
    var ingestor =
        EmbeddingStoreIngestor.builder()
            .embeddingModel(embeddingModel)
            .embeddingStore(embeddingStore)
            .build();

    System.out.println("ドキュメントを解析・学習中...");
    ingestor.ingest(documents);

    // 5. RAGエンジンの構築
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
        .start(); // ストリーミングの開始
    future.get();
  }
}
