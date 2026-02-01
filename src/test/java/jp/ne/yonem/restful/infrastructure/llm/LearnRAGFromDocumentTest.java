package jp.ne.yonem.restful.infrastructure.llm;

import java.util.concurrent.ExecutionException;

class LearnRAGFromDocumentTest {

  LearnRAGFromDocument sut = new LearnRAGFromDocument();
  String DIRECTORY_PATH = "rag";

  //  @Test
  void test01() throws ExecutionException, InterruptedException {
    var question = "私はエンジニアなのですが、10月15日のイベントには参加しなければなりませんか？";
    sut.execute(DIRECTORY_PATH, question);
  }

  //  @Test
  void test02() throws ExecutionException, InterruptedException {
    var question = "本年度に予定されているイベントをMD形式で一覧化してください";
    sut.execute(DIRECTORY_PATH, question);
  }
}
