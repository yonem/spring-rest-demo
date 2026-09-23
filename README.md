# Spring REST Demo

Java / Spring BootでWeb APIの設計・実装を学ぶための個人学習リポジトリです。認証、データベースアクセス、非同期処理、外部サービス連携、テスト、設計パターンを題材に、実装と検証を進めています。

本番利用を目的とした完成品ではありません。学習用の実装例として、設計上の判断や改善の余地を確認できる状態を重視しています。

## 主な技術

- Java 21
- Spring Boot 3.4.7
- Spring Security / JWT
- Spring Data JDBC / MyBatis
- PostgreSQL / H2
- Kafka
- Maven
- Docker Compose
- JUnit 5 / ArchUnit

## プロジェクト構成

```text
src/main/java/jp/ne/yonem/restful/
├── application/      # ユースケース・アプリケーションサービス
├── config/            # Spring・セキュリティ設定
├── infrastructure/   # DB、外部サービス、設計パターンの実装
└── presentation/     # Controller、DTO、例外処理
src/main/resources/   # プロファイル別設定、SQL、メッセージ
src/test/             # 単体テスト、結合テスト、アーキテクチャテスト
compose.yaml          # ローカルPostgreSQL
pom.xml               # Mavenビルド定義
```

## 必要な環境

- JDK 21
- Docker Desktop（ローカルDBを使用する場合）

認証鍵・暗号化キー・データベース認証情報は、リポジトリへ保存せず実行環境で設定してください。

```text
DB_USERNAME
DB_PASSWORD
ENCRYPTION_PASSWORD
JWT_PUBLIC_KEY
JWT_PRIVATE_KEY
```

`JWT_PUBLIC_KEY` と `JWT_PRIVATE_KEY` はRSA鍵のBase64値を指定します。公開済みの鍵は再利用せず、環境ごとに再生成してください。

## 起動方法

### 1. PostgreSQLを起動

```bash
docker compose up -d
```

### 2. 環境変数を設定

開発環境では、`DB_USERNAME`、`DB_PASSWORD`、`ENCRYPTION_PASSWORD`、`JWT_PUBLIC_KEY`、`JWT_PRIVATE_KEY`を設定します。詳細な既定値とプロファイル別設定は、`src/main/resources/application-*.properties`を確認してください。

### 3. アプリケーションを起動

Windows:

```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=dev"
```

macOS / Linux:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## ビルドとテスト

ビルドツールはMavenに統一しています。

```text
mvn clean verify
mvn test
```

コミット時には、Javaファイルがステージされている場合にpre-commitフックが`mvn spotless:check test`を実行します。フックのブランチ制限とJavaファイル変更判定は従来どおりです。

GitHub Actionsでは、`develop`へのpushとpull requestで、整形チェック、ビルド、テスト、秘密情報スキャンを実行します。

## APIドキュメント

アプリケーション起動後、SpringdocによるSwagger UIを利用できます。

```text
http://localhost:8080/swagger-ui/index.html
```

## 既知の制約

- 外部サービスやローカルインフラへの接続を必要とする機能があります。
- サンプル実装と検証用コードが同居しているため、用途ごとにパッケージを確認してください。
- 認証鍵などの秘密情報は、各自のローカル環境または安全なシークレット管理基盤で設定してください。
- 本番環境へ導入する場合は、セキュリティ、可用性、性能、監視、運用手順を別途設計・検証してください。

## Author

yonem

## License

このリポジトリは[MIT License](LICENSE)のもとで公開しています。
