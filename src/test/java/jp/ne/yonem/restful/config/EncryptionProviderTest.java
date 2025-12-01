package jp.ne.yonem.restful.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EncryptionProviderTest {
  private EncryptionProvider encryptionUtil;
  private static final String TEST_PASSWORD = "test-password-1234";

  @BeforeEach
  void setUp() {
    encryptionUtil = new EncryptionProvider(TEST_PASSWORD);
  }

  @Test
  @DisplayName("暗号化と復号化が正しく機能すること")
  void test01() throws Exception {
    var originalData = "Hello, JUnit 5!";
    var encryptedData = encryptionUtil.encrypt(originalData);
    var decryptedData = encryptionUtil.decrypt(encryptedData);

    assertNotEquals(originalData, encryptedData, "暗号化されたデータは元のデータと異なるべき");
    assertEquals(originalData, decryptedData, "復号化されたデータは元のデータと一致すべき");
  }

  @Test
  @DisplayName("異なるデータが正しく暗号化されること")
  void test02() throws Exception {
    var data1 = "Data to encrypt 1";
    var data2 = "Data to encrypt 2";
    var encryptedData1 = encryptionUtil.encrypt(data1);
    var encryptedData2 = encryptionUtil.encrypt(data2);

    assertNotEquals(encryptedData1, encryptedData2, "異なる入力は異なる暗号化結果になるべき");
  }

  @Test
  @DisplayName("同じデータは常に同じ結果に暗号化されること")
  void test03() throws Exception {
    var data = "Consistent encryption";
    var encryptedData1 = encryptionUtil.encrypt(data);
    var encryptedData2 = encryptionUtil.encrypt(data);

    assertEquals(encryptedData1, encryptedData2, "同じ入力は同じ暗号化結果になるべき");
  }

  @Test
  @DisplayName("改行を含むテキストが正しく暗号化および復号化されること")
  void test04() throws Exception {
    var originalDataWithNewlines =
        """
        This is a test text.
        It contains multiple lines.
        Line 3.
        """;
    System.out.println(originalDataWithNewlines);
    var encryptedData = encryptionUtil.encrypt(originalDataWithNewlines);
    var decryptedData = encryptionUtil.decrypt(encryptedData);

    assertNotEquals(originalDataWithNewlines, encryptedData, "暗号化されたデータは元のデータと異なるべき");
    assertEquals(originalDataWithNewlines, decryptedData, "復号化されたデータは元のデータと一致すべき");
  }
}
