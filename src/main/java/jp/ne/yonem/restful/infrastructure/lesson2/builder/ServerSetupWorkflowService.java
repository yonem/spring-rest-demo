package jp.ne.yonem.restful.infrastructure.lesson2.builder;

import java.util.Objects;
import org.springframework.stereotype.Service;

/** ServerConfigurationの構築とセットアップを行うサービスです。 */
@Service
public class ServerSetupWorkflowService {

  /**
   * 指定されたパラメータでサーバー設定オブジェクトを構築し、要約文字列を返却します。
   *
   * @param hostName ホスト名
   * @param port ポート番号
   * @param maxConnections 最大接続数
   * @param sslEnabled SSL有効化フラグ
   * @return サーバー構築サマリー
   */
  public String execute(String hostName, int port, int maxConnections, boolean sslEnabled) {
    var safeHostName = Objects.requireNonNull(hostName, "hostName must not be null");

    var config =
        ServerConfiguration.builder(safeHostName)
            .port(port)
            .maxConnections(maxConnections)
            .sslEnabled(sslEnabled)
            .build();

    return "Server[%s:%d] Connections:%d SSL:%b"
        .formatted(
            config.getHostName(),
            config.getPort(),
            config.getMaxConnections(),
            config.isSslEnabled());
  }
}
