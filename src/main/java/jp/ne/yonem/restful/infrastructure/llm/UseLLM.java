package jp.ne.yonem.restful.infrastructure.llm;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.ollama.OllamaChatModel;

public class UseLLM {

  public void execute(String question) {

    // 1. OllamaChatModelを使用する
    var model =
        OllamaChatModel.builder().baseUrl("http://localhost:11434").modelName("llama3").build();

    System.out.println("AIに質問中...");

    // 2. システムメッセージで「日本語で話す」役割を与え、ユーザーの質問とセットで送る
    var systemMessage = SystemMessage.from("あなたは誠実な日本人アシスタントです。必ず日本語で回答してください。");
    var userMessage = UserMessage.from(question);

    // 3. 回答を得る (generateメソッドの引数にメッセージのリストを渡す)
    var response = model.chat(systemMessage, userMessage);

    // 4. 結果を表示 (ChatResponseからテキスト内容を取り出す)
    System.out.println("--- AIの回答 ---");
    System.out.println(response.aiMessage().text());
  }
}
