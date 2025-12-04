package jp.ne.yonem.restful.application;

import static com.google.zxing.common.StringUtils.SHIFT_JIS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class UserCsvUploadServiceTest {
  private UserCsvUploadService sut;

  @BeforeEach
  void setUp() {
    sut = new UserCsvUploadService();
  }

  @Test
  @DisplayName("正常系: ダブルクォートなしのCSVを正しくパースできること")
  void test01() throws IOException {
    var csvContent = "氏名,年齢\nAlice,25\nBob,30";
    var inputStream = new ByteArrayInputStream(csvContent.getBytes(SHIFT_JIS));
    var mockFile = mock(MultipartFile.class);
    when(mockFile.getInputStream()).thenReturn(inputStream);

    var users = sut.execute(mockFile);
    assertEquals(2, users.size(), "ユーザーの数が期待値と一致しません");
    assertEquals(new UserCsvUploadService.User("Alice", 25), users.get(0));
    assertEquals(new UserCsvUploadService.User("Bob", 30), users.get(1));
  }

  @Test
  @DisplayName("正常系: ダブルクォートありのCSVを正しくパースできること")
  void test02() throws IOException {
    var csvContent = "\"氏名\",\"年齢\"\n\"Alice\",\"25\"\n\"Bob\",30";
    var inputStream = new ByteArrayInputStream(csvContent.getBytes(SHIFT_JIS));
    var mockFile = mock(MultipartFile.class);
    when(mockFile.getInputStream()).thenReturn(inputStream);

    var users = sut.execute(mockFile);
    assertEquals(2, users.size(), "ユーザーの数が期待値と一致しません");
    assertEquals(new UserCsvUploadService.User("Alice", 25), users.get(0));
    assertEquals(new UserCsvUploadService.User("Bob", 30), users.get(1));
  }

  @Test
  @DisplayName("異常系: 空のCSVファイルを渡された場合に空のリストを返すこと")
  void test03() throws IOException {
    var csvContent = "";
    var inputStream = new ByteArrayInputStream(csvContent.getBytes(SHIFT_JIS));
    var mockFile = mock(MultipartFile.class);
    when(mockFile.getInputStream()).thenReturn(inputStream);

    var users = sut.execute(mockFile);
    assertEquals(0, users.size(), "空のファイルの場合、空のリストを返す必要があります");
  }

  @Test
  @DisplayName("異常系: ageが数値でない場合にNumberFormatExceptionがスローされること")
  void test04() throws IOException {
    var csvContent = "氏名,年齢\nAlice,invalid_age\nBob,30";
    var inputStream = new ByteArrayInputStream(csvContent.getBytes(SHIFT_JIS));
    var mockFile = mock(MultipartFile.class);
    when(mockFile.getInputStream()).thenReturn(inputStream);

    assertThrows(NumberFormatException.class, () -> sut.execute(mockFile));
  }
}
