package jp.ne.yonem.restful.infrastructure;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class ZipCreationProviderTest {
  ZipCreationProvider sut = new ZipCreationProvider();

  private Path tempDir;
  private Path dummyFile1;
  private Path dummyFile2;
  private List<String> filePaths;
  private final String password = "password";

  @BeforeEach
  void setUp() throws IOException {
    tempDir = Files.createTempDirectory("zip_test_");
    dummyFile1 = Files.createFile(tempDir.resolve("fileA.txt"));
    dummyFile2 = Files.createFile(tempDir.resolve("fileB.txt"));
    Files.writeString(dummyFile1, "Content A");
    Files.writeString(dummyFile2, "Content B");
    filePaths = Arrays.asList(dummyFile1.toString(), dummyFile2.toString());
  }

  @AfterEach
  void tearDown() throws IOException {
    Files.deleteIfExists(dummyFile1);
    Files.deleteIfExists(dummyFile2);
    Files.deleteIfExists(tempDir);
  }

  @Test
  @DisplayName("正常系 - パスワードあり")
  void test01() throws IOException {
    var result = sut.download(filePaths, password);
    assertNotNull(result);
    assertNotNull(result.getFile());
    assertTrue(10 < result.getFile().length);

    var header = result.getHeader();
    assertNotNull(header);

    var contentDisposition = header.getFirst(HttpHeaders.CONTENT_DISPOSITION);
    assertNotNull(contentDisposition);
    System.out.printf("contentDisposition: %s%n", contentDisposition);
    assertTrue(contentDisposition.contains("filename=\"archive_"));
    assertEquals(MediaType.parseMediaType("application/zip"), header.getContentType());
    assertEquals(result.getFile().length, header.getContentLength());
  }

  @Test
  @DisplayName("正常系 - パスワードなし: 空文字")
  void test02() throws IOException {
    var result = sut.download(filePaths, "");
    assertNotNull(result);
    assertNotNull(result.getFile());
    assertTrue(10 < result.getFile().length);

    var header = result.getHeader();
    assertNotNull(header);

    var contentDisposition = header.getFirst(HttpHeaders.CONTENT_DISPOSITION);
    assertNotNull(contentDisposition);
    System.out.printf("contentDisposition: %s%n", contentDisposition);
    assertTrue(contentDisposition.contains("filename=\"archive_"));
    assertEquals(MediaType.parseMediaType("application/zip"), header.getContentType());
    assertEquals(result.getFile().length, header.getContentLength());
  }

  @Test
  @DisplayName("正常系 - パスワードなし: NULL")
  void test03() throws IOException {
    var result = sut.download(filePaths);
    assertNotNull(result);
    assertNotNull(result.getFile());
    assertTrue(10 < result.getFile().length);

    var header = result.getHeader();
    assertNotNull(header);

    var contentDisposition = header.getFirst(HttpHeaders.CONTENT_DISPOSITION);
    assertNotNull(contentDisposition);
    System.out.printf("contentDisposition: %s%n", contentDisposition);
    assertTrue(contentDisposition.contains("filename=\"archive_"));
    assertEquals(MediaType.parseMediaType("application/zip"), header.getContentType());
    assertEquals(result.getFile().length, header.getContentLength());
  }

  @Test
  @DisplayName("異常系 - 存在しないファイルパスが渡された場合")
  void test04() {
    var nonExistentPath =
        Path.of(
            System.getProperty("java.io.tmpdir"),
            "non_existent_" + System.currentTimeMillis() + ".txt");
    var invalidFilePaths = List.of(nonExistentPath.toString());
    assertThrows(IOException.class, () -> sut.download(invalidFilePaths, password));
  }
}
