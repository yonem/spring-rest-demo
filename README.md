# Spring REST Demo

> **学習・技術検証用リポジトリ**
>
> 本リポジトリは、Java/Springを中心に、Web API、認証、非同期処理、外部サービス連携、テスト、設計パターン、AI駆動開発などを検証するための学習用リポジトリです。
> 本番環境への投入を目的としたものではなく、技術検証と設計理解を主な目的としています。設定例や実装には、実運用前にセキュリティ、可用性、性能、運用性などの観点から追加の設計・検証が必要な箇所があります。

# セットアップガイド

## IntelliJ IDEAにgoogle-java-formatプラグインをインストール

1. **Settings**  
   Windows/Linux: `File` -\> `Settings`  
   macOS: `IntelliJ IDEA` -\> `Preferences`


2. **プラグインの検索とインストール**  
   `Plugins`  
   `Marketplace` -\> 検索バー`google-java-format`  
   `google-java-format`プラグインの`Install`ボタンをクリック


3. **IntelliJ IDEAの再起動**  
   プラグインのインストール後、IntelliJ IDEAの再起動


5. **google-java-formatの設定**  
   `ヘルプ` -\> `カスタム VM オプションの編集`

    ```
    --add-exports=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED
    --add-exports=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED
    --add-exports=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED
    --add-exports=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED
    --add-exports=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED
    --add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED
    ```

参考
[google-java-format IntelliJ Plugin](https://github.com/google/google-java-format?tab=readme-ov-file#intellij-jre-config)

-----

## PostgreSQLコンテナの作成 (Docker Compose)

Docker Composeを使用してPostgreSQLデータベースコンテナを起動

1. **`compose.yaml`ファイルの作成**  
   プロジェクトのルートディレクトリに`compose.yaml`（または`docker-compose.yml`）という名前のファイルを作成し、以下の内容を記述

   ```yaml
   version: '3.8'
   services:
     db:
       image: postgres:17.5
       restart: always
       environment:
         POSTGRES_DB: your_dev_db         # 任意のデータベース名に変更
         POSTGRES_USER: your_user         # 任意のユーザー名に変更
         POSTGRES_PASSWORD: your_password # 任意のパスワードに変更
       ports:
         - "5432:5432"
       volumes:
         - db_data:/var/lib/postgresql/data

   volumes:
     db_data:
   ```

   **注意:** `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD` は実際の値に置き換える


2. **PostgreSQLコンテナの起動**  
   `compose.yaml`ファイルを作成したディレクトリで、WSL (Ubuntu) のターミナルを開き、以下のコマンドを実行

   ```bash
   docker compose up -d
   ```

3. **コンテナの停止**

   ```bash
   docker compose stop
   ```

参考URL:
[Docker Compose CLI overview](https://docs.docker.com/compose/reference/)

-----

## 自己署名証明書でのSSL設定

SSL認証でHTTPS通信を有効にする

### 自己署名証明書（キーストアファイル）の作成

自己署名証明書を含むキーストアファイル（ここではPKCS#12形式の`.p12`ファイル）を作成する  
※ Java Development Kit (JDK) がインストールされていること（keytoolコマンドを使用する）

- キーストアファイルの生成

   ```bash
   keytool -genkeypair -alias your_key_alias -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keystore.p12 -validity 365 -dname "CN=your_domain_name, OU=YourOrgUnit, O=YourOrg, L=YourCity, ST=YourState, C=YourCountry" -storepass your_keystore_password -keypass your_keystore_password
   ```

    - コマンドの説明:
        - `genkeypair`: 鍵ペア（秘密鍵と公開鍵）を生成する
        - `alias your_key_alias`: 生成する鍵ペアに付ける名前（エイリアス）
        - `keyalg RSA`: 鍵のアルゴリズムにRSAを使用する
        - `keysize 2048`: 鍵の長さを2048ビットにします（標準的な長さ）
        - `storetype PKCS12`: キーストアの形式をPKCS#12にするSpring Bootで推奨される形式
        - `keystore keystore.p12`: 生成されるキーストアファイルの名前このファイルが重要
        - `validity 365`: 証明書の有効期間を365日（1年間）にする
        - `dname "..."`: 証明書に含める情報  
          `CN=your_domain_name`: Common Name（コモンネーム）ここにアクセスするドメイン名（例: localhost）を指定する  
          `OU=YourOrgUnit, O=YourOrg, L=YourCity, ST=YourState, C=YourCountry`: 組織名、場所などの情報適当な値でOK
        - `storepass your_keystore_password`: キーストア全体のパスワード
        - `keypass your_keystore_password`: 鍵のパスワードです。通常はキーストアのパスワードと同じにする

このコマンドを実行すると、`keystore.p12`というファイルが、コマンドを実行したディレクトリに作成されるので
`src/main/resources/`に配置する

### Spring Bootの`application.properties`を設定

`src/main/resources/application.properties`ファイルを開き、以下の設定を追加する

   ```properties
# HTTPSでアクセスを受け付けるポート番号を443に設定（任意、デフォルトは8443）
server.port=8443
# SSL（HTTPS）を有効にする
server.ssl.enabled=true
# キーストアのタイプをPKCS12に指定
server.ssl.key-store-type=PKCS12
# キーストアファイルがどこにあるかを指定
# `classpath:` は `src/main/resources/` の中を意味する
server.ssl.key-store=classpath:keystore.p12
# キーストアを開くためのパスワード（ステップ1で設定したパスワード）
server.ssl.key-store-password=your_keystore_password
# キーストアの中の鍵（証明書）のエイリアス（ステップ1で設定したエイリアス）
server.ssl.key-alias=your_key_alias
# 鍵自体のパスワード（ステップ1で設定したパスワード）
server.ssl.key-password=your_keystore_password
   ```

- `server.port`: HTTPSのポート番号。通常は`443`だが、ローカルでのテストでは`8443`を使うことが多い
- `your_keystore_password`と`your_key_alias`は、自己署名証明書で設定した値に合わせる

### 設定確認

Springアプリケーションを起動し`https://localhost:8443/` にアクセスする (`server.port`を`8443`に設定した場合)

-----

## CAからSSL証明書を取得 (参考)

`.p12`ファイルは、秘密鍵と証明書がセットになったJavaで扱いやすい形式となる。CAに認証してもらうプロセスは、通常、以下の流れ

- 秘密鍵（Private Key）の準備

  ```properties
   # .p12ファイルから秘密鍵を抽出するコマンド例 (OpenSSLが必要)
   openssl pkcs12 -in your_keystore.p12 -nodes -nocerts -out private.key
   # -nodes: 秘密鍵をパスワードなしで出力 (注意: 安全な場所に保管！)
   # -nocerts: 証明書は出力しない
   # -out private.key: 出力ファイル名
   ```

- CSR（Certificate Signing Request）の生成

   ```properties
   # CSRを生成するコマンド例 (OpenSSLが必要)
   openssl req -new -key private.key -out your_domain.csr -sha256 -dname "CN=your_domain.com, O=Your Organization, L=Your City, ST=Your State, C=JP"
   ```

    - コマンドの説明:
        - `key private.key`: ステップ1で抽出した秘密鍵を指定
        - `out your_domain.csr`: 生成されるCSRファイルの名前
        - `sha256`: 署名アルゴリズムを指定
        - `dname "..."`: 証明書に含める情報を指定。`CN (Common Name)` は、ウェブサイトの正確なドメイン名 (例:
          `www.example.com` や `api.example.com`) を必ず入力する

- CA（認証局）への申請と審査  
  生成した`your_domain.csr`ファイルを、選択したCA（例: `Let's Encrypt`、`GMOグローバルサイン`、`Symantec`、`DigiCert`
  など）のウェブサイトから申請する
    - CAの選択
        - 無料かつ自動化がしやすい: `Let's Encrypt`（テスト環境や個人ブログなどに最適）
        - 法人向けで信頼性が高い（有料）: `GMOグローバルサイン`、`Symantec`、`DigiCert`など
- 申請プロセス
    - CAのウェブサイトで、希望する証明書の種類（ドメイン認証、組織認証、EV認証など）を選択し、申し込みを開始する
    - 生成した`your_domain.csr`ファイルの内容を、CAのフォームに貼り付ける
    - CAは、ドメインの所有者であることを確認するために、いくつかの審査方法を提示する
        - （例: ドメインのDNSレコードに特定の情報を追加する、特定のファイルをウェブサイトのルートディレクトリに配置する、登録メールアドレスに確認メールを送るなど）
    - 審査に合格すると、CAから **「正式なデジタル証明書」ファイル**
      が発行される。通常、これはサーバー証明書と中間証明書（バンドル証明書）がセットになった`.pem`形式や`.crt`
      形式のファイルとして提供される

- 発行された証明書を`.p12`形式に変換し、Spring Bootに設定  
  CAから発行された証明書は、通常`.pem`や`.crt`形式となる。これをSpring Bootで扱いやすい`.p12`形式に変換し直す
- CAから受け取った証明書と、元の秘密鍵を使って`.p12`を作成する  
  CAから提供された`your_domain.crt` (サーバー証明書) と `ca_bundle.crt` (
  中間証明書。通常CAから提供されるか、CAのサイトでダウンロード可能) を準備する
   ```properties
   # 証明書チェーンを作成（サーバー証明書と中間証明書を結合）
   cat your_domain.crt ca_bundle.crt > fullchain.pem
   # fullchain.pem と private.key を使って .p12 ファイルを生成
   openssl pkcs12 -export -in fullchain.pem -inkey private.key -out trusted_keystore.p12 -name your_alias -password pass:
   your_keystore_password
   ```
    - コマンドの説明:
        - `in fullchain.pem`: サーバー証明書と中間証明書を結合したファイル
        - `inkey private.key`: あなたの秘密鍵ファイル
        - `out trusted_keystore.p12`: 生成される新しいPKCS#12ファイル名
        - `name your_alias`: キーストア内のエイリアス
        - `password pass:your_keystore_password`: 新しいキーストアのパスワード
- `application.properties`の設定を更新  
  新しい`trusted_keystore.p12`ファイルを`src/main/resources/`に配置し、`application.properties`のパスとパスワードを更新する

   ```properties
   server.ssl.key-store=classpath:trusted_keystore.p12 # ファイル名を更新
   server.ssl.key-store-password=your_keystore_password # パスワードを更新
   server.ssl.key-alias=your_alias # エイリアスを更新
   server.ssl.key-password=your_keystore_password # パスワードを更新
   ```

---

## Gemini API (Google AI Studio) の設定

Gemini API を使用して LLM 機能を利用するための設定

### API キーの取得

1. **Google AI Studio にアクセス** [Google AI Studio](https://aistudio.google.com/) にアクセスし、Google アカウントでログイン
2. **API キーの生成** 左サイドメニューの `Get API key` をクリック
   `Create API key in new project` ボタンをクリックして API キーを発行
3. **API キーのコピー** 発行された文字列をコピーして安全な場所に保管する
   ※ API キーは公開リポジトリ（GitHub等）にコミットしないよう注意

### Spring Boot の `application.properties` を設定

`src/main/resources/application.properties` に取得したキーを追加（キーを直接記述せず環境変数から読み込む設定を推奨）

```properties
# Gemini API 設定
# 直接記述する場合: langchain4j.google-ai-gemini.api-key=AIza...
# 環境変数から読み込む場合 (推奨)
langchain4j.google-ai-gemini.api-key=${GEMINI_API_KEY}
langchain4j.google-ai-gemini.model-name=gemini-3-flash-preview

```

### 依存関係の追加 (Gradle)

`build.gradle.kts` に以下の依存関係を追加

```kts
implementation("dev.langchain4j:langchain4j-google-ai-gemini:1.10.0")
```

### 参考

[Gemini API ドキュメント (日本語)](https://ai.google.dev/gemini-api/docs?hl=ja)
