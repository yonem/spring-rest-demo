package jp.ne.yonem.restful.infrastructure;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import jp.ne.yonem.restful.presentation.dto.DownloadFileResponse;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ZipCreationService {
  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
  private static final String ZIP_BASE_NAME = "archive_";
  private static final String ZIP_EXTENSION = ".zip";

  /**
   * 指定されたファイルパスのファイルをZIPに圧縮し、 ダウンロードに必要なヘッダーとバイト配列をDTOで返す唯一の実行メソッド。
   *
   * @param filePaths 圧縮対象のファイルのパスのリスト
   * @return ダウンロードに必要な情報を含む DownloadFileResponse
   * @throws IOException ZIP作成またはファイル操作中にエラーが発生した場合
   */
  public DownloadFileResponse execute(List<String> filePaths) throws IOException {
    return execute(filePaths, null);
  }

  /**
   * 指定されたファイルパスのファイルをパスワード付きでZIPに圧縮し、 ダウンロードに必要なヘッダーとバイト配列をDTOで返す唯一の実行メソッド。
   *
   * @param filePaths 圧縮対象のファイルのパスのリスト
   * @param password ZIPファイルに設定するパスワード
   * @return ダウンロードに必要な情報を含む DownloadFileResponse
   * @throws IOException ZIP作成またはファイル操作中にエラーが発生した場合
   */
  public DownloadFileResponse execute(List<String> filePaths, String password) throws IOException {
    var timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
    var zipFileName = "%s%s%s".formatted(ZIP_BASE_NAME, timestamp, ZIP_EXTENSION);
    var targetFiles = filePaths.stream().map(File::new).collect(Collectors.toList());
    var tempZipPath = Path.of(System.getProperty("java.io.tmpdir"), zipFileName);
    var hasPassword = password != null && !password.isEmpty();
    var zipFile =
        hasPassword
            ? new ZipFile(tempZipPath.toFile(), password.toCharArray())
            : new ZipFile(tempZipPath.toFile());

    var parameters = new ZipParameters();
    parameters.setRootFolderNameInZip(
        "%s%s".formatted(zipFileName.replace(ZIP_EXTENSION, ""), File.separator));
    parameters.setEncryptFiles(hasPassword);
    parameters.setEncryptionMethod(EncryptionMethod.AES);
    parameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);

    try (zipFile) {
      zipFile.addFiles(targetFiles, parameters);
      var zipBytes = Files.readAllBytes(tempZipPath);
      var headers = new HttpHeaders();
      headers.setContentDispositionFormData("attachment", zipFileName); // ファイル名を指定
      headers.setContentType(MediaType.parseMediaType("application/zip"));
      headers.setContentLength(zipBytes.length);
      return new DownloadFileResponse(headers, zipBytes);

    } catch (ZipException e) {
      throw new IOException("Failed to create password-protected ZIP file.", e);

    } finally {
      Files.deleteIfExists(tempZipPath);
      log.info("Temp file deleted. : {}", tempZipPath);
    }
  }
}
