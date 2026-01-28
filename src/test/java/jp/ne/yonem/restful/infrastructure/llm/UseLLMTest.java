package jp.ne.yonem.restful.infrastructure.llm;

class UseLLMTest {

  UseLLM sut = new UseLLM();

  //  @Test
  void test01() {
    sut.execute("JavaエンジニアがLLMを学ぶメリットを3点教えて。");
  }
}
