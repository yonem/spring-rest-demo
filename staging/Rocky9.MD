# Staging環境 模擬デプロイ手順 (Rocky 9 + Docker)

このドキュメントは、Java Spring Boot APIシステムをRocky
Linux9環境にデプロイし、サービスとして運用するためのローカル検証手順をまとめたものです。Dockerを使用して、ターゲットサーバー（Locky9）の環境を模擬します。

## 前提条件

* Dockerがインストールされ、実行可能であること。
* ローカルPCにMavenがインストールされ、Spring Bootアプリケーションのビルドが可能であること。
* Spring Bootアプリケーションのプロジェクトルートで作業していること。

## 1\. ターゲット環境の準備 (Docker)

Locky9サーバーの環境、ユーザー、JDK (Temurin 21 via SDKMAN\!) を設定したDockerコンテナを準備します。

### 1.1 $\text{Dockerfile}$の作成

$\text{Dockerfile}$を作成します。

### 1.2 サービス起動スクリプト ($\text{start-service.sh}$) の作成

`systemd`を簡易的に模倣し、アプリケーションをバックグラウンドで実行・再起動するためのスクリプトです。

### 1.3 コンテナのビルドと起動

$\text{Dockerfile}$と$\text{start-service.sh}$を同じディレクトリに配置し、ビルド・起動します。

```bash
# 1. コンテナイメージをビルド
docker build -t rocky9-stg-env .

# 2. コンテナをバックグラウンドで起動 (ホストの8083ポートをコンテナにマッピング)
docker run -d --name stg-server -p 8083:8083 rocky9-stg-env

# 3. 起動ログの確認
# アプリケーションがまだデプロイされていないため、「Waiting for deployment...」と表示されます。
docker logs -f stg-server
```

## 2\. アプリケーションのビルドとデプロイ

### 2.1 アプリケーションのビルド

ローカルでSpring Bootアプリケーションをビルドします。

```bash
# 1. MavenでJARファイルを作成
mvn clean package -DskipTests

# 2. プロパティファイルが src/main/resources/ にあることを確認
# (例: application-stg.properties)
```

### 2.2 ファイルコピー (デプロイ)

ビルドしたJARファイルとステージング用プロパティファイルを、実行中のコンテナにコピーします。

```bash
# 1. JARファイルのパスを取得 (バージョン名は適宜変更してください)
JAR_PATH=$(find target -name 'api-service-*.jar' | head -n 1)

# 2. JARファイルをコンテナ内の実行ファイル名 app.jar としてコピー
docker cp ${JAR_PATH} stg-server:/opt/api-service/app.jar

# 3. プロパティファイルをコピー
docker cp src/main/resources/application-stg.properties stg-server:/opt/api-service/application-stg.properties
```

### 2.3 サービスの再起動（デプロイ後の処理）

実際のCI/CDでは`systemctl restart`
に相当する操作を行います。ここでは、実行中のプロセスを強制終了することで、$\text{start-service.sh}$による自動再起動を誘発します。

```bash
# 1. コンテナ内で実行中のプロセスIDを取得し、強制終了
# (start-service.sh がすぐにサービスを再起動します)
docker exec stg-server /bin/bash -c "kill -TERM \$(cat /var/run/api-service.pid)"
```

**【重要】CI/CDでの実際のデプロイコマンド**

本番環境では、上記のプロセス終了操作の代わりに、SSH経由で以下のコマンドを実行します。

```bash
# Locky9サーバーで実行するコマンド (SSH経由を想定)
sudo systemctl restart api-service
```

## 3\. 動作確認

サービスがステージングプロファイルで起動し、外部からアクセスできることを確認します。

```bash
# 1. コンテナログでアプリケーションの起動を確認
docker logs -f stg-server

# 2. APIエンドポイントへアクセス (ポート8083)
curl http://localhost:8083/your/api/endpoint
```
