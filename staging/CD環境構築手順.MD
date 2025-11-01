# 📄 CD環境構築手順

このドキュメントは、Java Spring Boot APIシステムのコンテナ環境を単一サーバー上に構築し、**初回のDBマイグレーション**を実行するための手順をまとめたものです。

-----

## 0\. 前提条件と環境クリーンアップ

### 0.1 前提条件

  * DockerとDocker Composeがインストールされ、実行可能であること。
  * `docker-compose.yml`, `docker-compose-stg.yml`を含むすべての環境定義ファイルが揃っていること。

### 0.2 環境のクリーンアップ（初期化時のみ）

既存のコンテナ、ネットワーク、永続化ボリューム（DBデータ）を削除し、環境を初期化します。

```bash
# プロジェクトディレクトリに移動
cd ~/workspace/staging/

# 全サービスを停止し、コンテナを削除
docker compose -f docker-compose.yml -f docker-compose-stg.yml down

# データベースの永続化ボリューム（db_data）も削除し、DBを初期化
# ★警告: 開発環境の初期化時のみ実行してください。
docker compose -f docker-compose.yml -f docker-compose-stg.yml down -v
```

-----

## 1\. ターゲット環境とログ集約システムの準備 (Docker Compose)

Javaアプリケーションの実行環境 (`stg-server`)、データベース (`db`)、ログ集約システム (`elasticsearch-host`, `kibana-host`) などを一括で起動します。

### 1.1 コンテナイメージのビルドと全サービスの起動

`Dockerfile`を参照してJavaアプリのベースイメージをビルドし、すべてのサービスをバックグラウンドで起動します。

```bash
# コンテナイメージをビルドし、全サービスをバックグラウンドで起動
docker compose -f docker-compose.yml -f docker-compose-stg.yml up -d --build
```

### 1.2 起動状態の確認

すべてのコンテナが稼働中であることを確認します。`stg-server`はアプリケーション（JARファイル）がないため、待機状態（ログに`Waiting for deployment...`）で正常です。

```bash
docker compose -f docker-compose.yml -f docker-compose-stg.yml ps
```

-----

## 2\. 初回DBマイグレーションの実行（アプリケーションデプロイ前に必須）

データベースコンテナ (`db`) が起動していることを確認した後、FlywayやLiquibaseなどのマイグレーションツールを使って、データベースのスキーマを初期化します。

> **注:** 通常、マイグレーションはCI/CDパイプラインのデプロイ直前のジョブとして実行しますが、環境構築時の初期設定として手動で実行します。

### 2.1 マイグレーション実行用の作業コンテナの起動

一時的なマイグレーション用コンテナを起動し、マイグレーションを実行します。

```bash
# Flyway SQLファイルが格納されているローカルディレクトリのパス
MIGRATION_DIR="migration" 
FLYWAY_IMAGE="flyway/flyway:10.10.0-alpine"

# データベース接続情報（application-stg.propertiesより）
DB_HOST="db"  # docker-compose-stg.yml のサービス名
DB_NAME="your_dev_db"
DB_USER="your_user"
DB_PASS="your_password"

# Flyway CLI コンテナを使用してマイグレーションを実行
docker run --rm \
  --network staging_default \
  -v "$(pwd)/${MIGRATION_DIR}:/flyway/sql" \
  ${FLYWAY_IMAGE} \
  -url="jdbc:postgresql://${DB_HOST}:5432/${DB_NAME}" \
  -user="${DB_USER}" \
  -password="${DB_PASS}" \
  migrate
```

-----

## 3\. アプリケーションのデプロイ（初回の配置）

デプロイ手順は、CI/CDパイプラインが実行するコマンドと同じロジックに従います。

### 3.1 ファイルコピー (デプロイ)

ビルドしたJARファイルとステージング用設定ファイルを、実行中の`stg-server`コンテナにコピーします。

```bash
# 1. JARファイルのパスを取得 (バージョン名は適宜変更してください)
JAR_PATH=$(find target -name 'restful-*.jar' | head -n 1)

# 2. JARファイルをコンテナ内の実行ファイル名 app.jar としてコピー
docker cp ${JAR_PATH} stg-server:/opt/api-service/app.jar

# 3. プロパティファイルとログ設定ファイルをコピー
docker cp application-stg.properties stg-server:/opt/api-service/application-stg.properties
docker cp logback-spring.xml stg-server:/opt/api-service/logback-spring-stg.xml
```

### 3.2 サービスの再起動（デプロイ後の処理）

ファイルのコピーが完了すると、`start-service.sh`が自動的にアプリケーションの存在を検知し、起動します。

再デプロイを行う場合は、実行中のプロセスを強制終了します。

```bash
# 1. コンテナ内で実行中のプロセスIDを取得し、強制終了
# (start-service.sh がすぐにサービスを再起動します)
docker exec stg-server /bin/bash -c "kill -TERM \$(pgrep -f 'java -jar /opt/api-service/app.jar')"
```

-----

## 4\. 動作確認

サービスが起動し、ログがElasticsearchに転送されていることを確認します。

```bash
# 1. stg-serverのコンテナログでアプリケーションの起動を確認
docker logs -f stg-server

# 2. Fluent Bitログを確認 (Elasticsearchへの転送状況を確認)
docker logs -f fluent-bit
# 「[es: info] flush_mode=async, status=200」のような転送成功を示すログを確認

# 3. APIエンドポイントへアクセス (ポート8083)
http://localhost:8083/
```

### ログのトレーサビリティ確認 (Kibana)

KibanaのWebインターフェースを通じて、ログが正しく集約されているか確認します。

1. ブラウザで **`http://localhost:5601`** にアクセスします。
2. 左側のメニューから **Analytics** -\> **Discover** を選択します。
3. `java-app-*`のデータビューを作成します。
4. APIアクセスによって発生したログがKibana上で検索・表示できることを確認します。
