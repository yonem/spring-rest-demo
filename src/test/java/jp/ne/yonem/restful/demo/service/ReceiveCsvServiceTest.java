package jp.ne.yonem.restful.demo.service;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class ReceiveCsvServiceTest {
  @InjectMocks private ReceiveCsvService sut;

  private MultipartFile mockCsvFile;
  private MultipartFile emptyFile;

  @BeforeEach
  void setUp() {
    var csvContent =
        """
        id,name,email
        1,Alice,alice@example.com
        2,Bob,bob@example.com
        """;

    // MockMultipartFileのインスタンスを作成
    mockCsvFile =
        new MockMultipartFile(
            "file", // パラメータ名
            "test_users.csv", // オリジナルファイル名
            "text/csv", // コンテンツタイプ
            csvContent.getBytes(UTF_8) // ファイルの内容
            );

    emptyFile =
        new MockMultipartFile(
            "file", "empty.csv", "text/csv", new byte[0] // 空のファイル
            );
  }

  @Test
  @DisplayName("正常系: CSVファイルの読み込み")
  void test01() {
    var act = sut.execute(1, mockCsvFile);
    assertEquals(2, act.getBody().size());
    System.out.println(act.getHeader());
    System.out.println("================");
    act.getBody().forEach(System.out::println);

    // 空ファイル
    act = sut.execute(1, emptyFile);
    assertEquals(0, act.getBody().size());
  }

  @Test
  @DisplayName("異常系: CSVファイルの読み込み時に例外が発生する")
  void test02() {
    var throwingFile =
        new MockMultipartFile("file", "test.csv", "text/csv", "test".getBytes()) {
          @Override
          public InputStream getInputStream() throws IOException {
            throw new IOException("Simulating an I/O error");
          }
        };
    assertThrows(RuntimeException.class, () -> sut.execute(1, throwingFile));
  }
}
