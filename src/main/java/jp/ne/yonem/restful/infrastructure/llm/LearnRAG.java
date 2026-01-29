package jp.ne.yonem.restful.infrastructure.llm;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.onnx.allminilml6v2q.AllMiniLmL6V2QuantizedEmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

public class LearnRAG {

  // AIのキャラクターを定義するインターフェース
  interface Assistant {

    // ここで役割を指定することで、回答を日本語に固定する
    @SystemMessage("あなたは親切な日本人アシスタントです。必ず日本語で回答してください。")
    String chat(String message);
  }

  public void execute() {

    // 1. LLMの設定
    var model =
        OllamaChatModel.builder().baseUrl("http://localhost:11434").modelName("llama3").build();

    // 2. 独自の知識（ドキュメント）を準備
    var info = "弊社のランチタイムは13時から14時までです。金曜日は特別なピザパーティーがあります。";
    var doc = Document.from(info);

    // 3. 知識をベクトル化してメモリに保存
    var embeddingStore = new InMemoryEmbeddingStore<TextSegment>();
    var embeddingModel = new AllMiniLmL6V2QuantizedEmbeddingModel();

    // 文書をベクトルに変換してストアに格納
    var embedding = embeddingModel.embed(doc.text()).content();
    embeddingStore.add(embedding, TextSegment.from(doc.text()));

    // 4. 検索エンジンの作成（質問に関連する知識を引っ張る役）
    var contentRetriever =
        EmbeddingStoreContentRetriever.builder()
            .embeddingStore(embeddingStore)
            .embeddingModel(embeddingModel)
            .maxResults(1) // 最も関連性の高い1件を取得
            .build();

    // 5. AIサービスの構築（ここで知識とモデルを合体させる）
    var assistant =
        AiServices.builder(Assistant.class)
            .chatModel(model)
            .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
            .contentRetriever(contentRetriever)
            .build();

    // 6. 実行
    System.out.println("AIに質問中...");
    var response = assistant.chat("金曜日のランチは何がありますか？");

    System.out.println("--- AIの回答 ---");
    System.out.println(response);
  }
}
