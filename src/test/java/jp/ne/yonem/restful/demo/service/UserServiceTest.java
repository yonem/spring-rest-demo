package jp.ne.yonem.restful.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
  @InjectMocks private UserService sut;

  @Test
  @DisplayName("ユーザーリストがCSV形式で正しく出力されること")
  void test01() throws Exception {
    var stringWriter = new StringWriter();
    var printWriter = new PrintWriter(stringWriter);
    var exp =
        String.join(System.lineSeparator(), "name,age", "Alice,25", "Bob,30", "Charlie,35", "");

    sut.execute(printWriter);
    assertEquals(exp.strip(), stringWriter.toString().strip());
  }
}
