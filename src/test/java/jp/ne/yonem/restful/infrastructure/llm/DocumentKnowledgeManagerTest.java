package jp.ne.yonem.restful.infrastructure.llm;

import static org.junit.jupiter.api.Assertions.*;

class DocumentKnowledgeManagerTest {

  DocumentKnowledgeManager sut = new DocumentKnowledgeManager();
  String DIRECTORY_PATH = "rag";

  //  @Test
  void test01() {
    var question = "本年度に予定されているイベントをMD形式で一覧化してください。最後にあいさつ文を添えてください。";
    sut.execute(DIRECTORY_PATH, question);
  }
}
