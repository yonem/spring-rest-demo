package jp.ne.yonem.restful.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
  @InjectMocks private UserCsvService sut;

  @Test
  @DisplayName("ユーザーリストがCSV形式で正しく出力されること")
  void test01() throws IOException {
    var exp = String.join(System.lineSeparator(), "name,age", "Alice,25", "Bob,30", "Charlie,35");
    var act = sut.execute();
    var file = new String(act.getFile());
    assertEquals(exp, file);
  }
}
