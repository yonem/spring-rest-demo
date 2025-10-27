# Staging環境 模擬デプロイ手順 (Rocky 9 + Docker)

このドキュメントは、Java Spring Boot APIシステムをRocky Linux9環境にデプロイし、サービスとして運用するためのローカル検証手順をまとめたものです。
**Docker Compose**を使用して、すべての環境（Javaアプリ、ログ集約システム）を模擬します。

## 前提条件

* Dockerがインストールされ、実行可能であること。
* **Docker Compose**がインストールされ、実行可能であること。
* ローカルPCにMavenがインストールされ、Spring Bootアプリケーションのビルドが可能であること。
* `Dockerfile`, `start-service.sh`, `fluent-bit.conf`, **`docker-compose.yml`** がプロジェクトルートに配置されていること。

-----

## 0\. ターゲット環境とログ集約システムの準備 (Docker Compose)

Javaアプリケーションの実行環境 (`stg-server`)、ログ集約システム (`elasticsearch-host`, `kibana-host`)
、およびログコレクタ (`fluent-bit`) を一括で起動します。

### 0.1 コンテナイメージのビルドと全サービスの起動

`docker-compose.yml`を参照し、Javaアプリのイメージをビルドし、すべてのサービスをバックグラウンドで起動します。

```bash
# 1. コンテナイメージをビルドし、全サービスをバックグラウンドで起動
# Javaアプリケーションのイメージビルドもここで行われます。
docker compose up -d --build

# 2. 起動状態の確認
docker compose ps
# すべてのコンテナ (stg-server, elasticsearch-host, kibana-host, fluent-bit) が
# Running状態、またはelasticsearch-hostが (healthy) になるまで待ちます。
```

### 0.2 ログの確認

アプリケーションがまだデプロイされていないため、`stg-server`は警告を表示して待機状態に入ります。

```bash
# 1. stg-serverの起動ログを確認
# 「WARNING: /opt/api-service/app.jar not found. Waiting for deployment...」と表示されます。
docker logs -f stg-server
```

-----

## 1\. アプリケーションのビルドとデプロイ

### 1.1 アプリケーションのビルド

ローカルでSpring Bootアプリケーションをビルドします。

```bash
# 1. MavenでJARファイルを作成
mvn clean package -DskipTests

# 2. プロパティファイルが src/main/resources/ にあることを確認
# (例: application-stg.properties)
```

### 1.2 ファイルコピー (デプロイ)

ビルドしたJARファイルとステージング用プロパティファイルを、実行中の`stg-server`コンテナにコピーします。

```bash
# 1. JARファイルのパスを取得 (バージョン名は適宜変更してください)
JAR_PATH=$(find target -name 'api-service-*.jar' | head -n 1)

# 2. JARファイルをコンテナ内の実行ファイル名 app.jar としてコピー
docker cp ${JAR_PATH} stg-server:/opt/api-service/app.jar

# 3. プロパティファイルをコピー
docker cp src/main/resources/application-stg.properties stg-server:/opt/api-service/application-stg.properties
```

### 1.3 サービスの再起動（デプロイ後の処理）

ファイルのコピーが完了すると、`start-service.sh`が自動的にアプリケーションの存在を検知し、起動します。再デプロイを行う場合は、実行中のプロセスを強制終了します。

```bash
# 1. コンテナ内で実行中のプロセスIDを取得し、強制終了
# (start-service.sh がすぐにサービスを再起動します)
docker exec stg-server /bin/bash -c "kill -TERM \$(pgrep -f 'java -jar /opt/api-service/app.jar')"
```

**【重要】CI/CDでの実際のデプロイコマンド**

本番環境では、上記のプロセス終了操作の代わりに、SSH経由で以下のコマンドを実行します。

```bash
# Locky9サーバーで実行するコマンド (SSH経由を想定)
sudo systemctl restart api-service
```

-----

## 2\. 動作確認

サービスが起動し、ログがElasticsearchに転送されていることを確認します。

```bash
# 1. stg-serverのコンテナログでアプリケーションの起動を確認
docker logs -f stg-server

# 2. Fluent Bitログを確認 (Elasticsearchへの転送状況を確認)
docker logs -f fluent-bit
# 「[es: info] flush_mode=async, status=200」のような転送成功を示すログを確認

# 3. APIエンドポイントへアクセス (ポート8083)
curl http://localhost:8083/your/api/endpoint
```

### ログのトレーサビリティ確認 (Kibana)

KibanaのWebインターフェースを通じて、ログが正しく集約されているか確認します。

1. ブラウザで **`http://localhost:5601`** にアクセスします。
2. 左側のメニューから **Analytics** -\> **Discover** を選択します。
3. `java-app-*`のデータビューを作成します。
4. APIアクセス(`curl`)によって発生したログがKibana上で検索・表示できることを確認します。
