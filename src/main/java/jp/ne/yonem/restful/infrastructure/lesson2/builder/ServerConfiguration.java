package jp.ne.yonem.restful.infrastructure.lesson2.builder;

import java.util.Objects;
import lombok.Getter;

/** サーバー構築構成情報を保持するドメインオブジェクトです。 */
@Getter
public final class ServerConfiguration {

  private final String hostName;
  private final int port;
  private final int maxConnections;
  private final boolean sslEnabled;

  private ServerConfiguration(Builder builder) {
    this.hostName = builder.hostName;
    this.port = builder.port;
    this.maxConnections = builder.maxConnections;
    this.sslEnabled = builder.sslEnabled;
  }

  public static Builder builder(String hostName) {
    return new Builder(hostName);
  }

  /** ServerConfiguration専用のBuilderクラスです。 */
  public static class Builder {

    private final String hostName; // 必須項目
    private int port = 8080; // 任意項目（デフォルト値あり）
    private int maxConnections = 100;
    private boolean sslEnabled = false;

    public Builder(String hostName) {
      this.hostName = Objects.requireNonNull(hostName, "hostName must not be null");
    }

    public Builder port(int port) {
      if (port <= 0 || port > 65535) {
        throw new IllegalArgumentException("port must be between 1 and 65535");
      }
      this.port = port;
      return this;
    }

    public Builder maxConnections(int maxConnections) {
      if (maxConnections <= 0) {
        throw new IllegalArgumentException("maxConnections must be greater than 0");
      }
      this.maxConnections = maxConnections;
      return this;
    }

    public Builder sslEnabled(boolean sslEnabled) {
      this.sslEnabled = sslEnabled;
      return this;
    }

    public ServerConfiguration build() {
      return new ServerConfiguration(this);
    }
  }
}
