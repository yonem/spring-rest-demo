package jp.ne.yonem.restful.infrastructure.llm;

import static org.junit.jupiter.api.Assertions.*;

class GeminiChatAssistantTest {

  GeminiChatAssistant sut = new GeminiChatAssistant();
  String DIRECTORY_PATH = "rag";

  //  @Test
  void test01() {
    var question = "本年度に予定されているイベントをMD形式で一覧化してください。最後にあいさつ文を添えてください。";
    sut.execute(DIRECTORY_PATH, question);
  }

  //  @Test
  void test02() {
    var question = "私はエンジニアなのですが、10月15日のイベントには参加しなければなりませんか？";
    sut.execute(DIRECTORY_PATH, question);
  }
}
