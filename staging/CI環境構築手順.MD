# 📄 CI環境構築手順 (Jenkins スタンドアロン)

このドキュメントは、Jenkinsのフリースタイル・プロジェクトを活用し、**ビルド**、**デプロイ**を自動化し、**DBマイグレーション**は手動で行うための手順をまとめたものです。

-----

## 0\. 前提条件とJenkinsの初期設定

### 0.1 前提条件

  * JenkinsサーバーがDockerコンテナとして稼働中であり、Web UI (`http://localhost:8080`) にアクセスできること。
  * JenkinsコンテナがホストのDockerデーモンにアクセス可能であること（`docker.sock`のマウント設定が完了していること）。
  * CI/CD対象のプロジェクトコードがホストサーバー上に存在していること。

### 0.2 Jenkinsの初期設定

1.  **初期パスワードの取得**:
      * Jenkinsコンテナのログから初期パスワードを取得します。
        ```bash
        docker logs jenkins-server
        ```
2.  **Web UIアクセスとロック解除**:
      * ブラウザで `http://localhost:8080` にアクセスし、取得したパスワードを入力してロックを解除します。
3.  **プラグインのインストール**:
      * 「**Install suggested plugins**」を選択し、推奨プラグインをインストールします。
4.  **管理者ユーザーの作成**:
      * 管理者ユーザーを作成し、セットアップを完了します。

-----

## 1\. Jenkinsセキュリティと環境設定

### 1.1 データベース認証情報 (Credentials) の登録

Flyway CLIで使用するDB接続情報をJenkinsに安全に登録します。

1.  ダッシューボードから「**Jenkinsの管理**」へ移動します。
2.  「**Security - Credentials**」（資格情報の管理）を選択し、**global**ドメインへ移動します。
3.  「**Add Credentials**」をクリックし、以下の情報を登録します。

| フィールド | UI上の日本語名 | 内容 |
| :--- | :--- | :--- |
| **Kind** | 種類 | `ユーザー名とパスワード` を選択 |
| **Username** | ユーザー名 | `your_user` |
| **Password** | パスワード | `your_password` |
| **ID** | ID | **`DB_CREDENTIALS_STG`** (ジョブで使用するID) |

### 1.2 GitとMavenの設定

「**Jenkinsの管理**」→ 「**Global Tool Configuration**」で、JDKとMavenをインストールし、設定します。

#### Mavenツールの設定手順

1.  「**Maven**」セクションを見つけ、「**Mavenの追加**」をクリックします。
2.  **Name** に **`M3_HOME`** (ジョブ設定で使用する名前) を入力します。
3.  「**Install automatically**」にチェックを入れ、適切なバージョンを選択します。

-----

## 2\. CI/CD ジョブの定義 (フリースタイル・プロジェクト)

### 2.1 新規ジョブの作成

1.  Jenkinsダッシュボードで左メニューの「**新規ジョブ作成**」をクリックします。
2.  **Item Name** に `Staging-Deploy-Freestyle` などの名前を入力し、「**フリースタイル・プロジェクトのビルド**」を選択して「**OK**」をクリックします。

### 2.2 ジョブの設定（General & ソースコード管理項目）

  * **General**: 必要に応じてジョブの説明を記述します。
  * **ソースコード管理**: GitリポジトリURLとアクセス認証情報（Credentials）を設定します。

### 2.3 ジョブの設定（ビルド手順項目）

「ビルド」セクションで、**Dockerクライアント**のインストールと、**ビルド**、**デプロイ**のステップを順番に追加します。

1.  **「ビルド環境」** セクションで「**Use secret text(s) or file(s) $\to$**」にチェックを入れます。
2.  **Credentials** で **`DB_CREDENTIALS_STG`** を選択し、環境変数を設定します。

| 変数名 | Bindings |
| :--- | :--- |
| `CRED_USR` | Username |
| `CRED_PSW` | Password |

#### ステップ 0: Dockerクライアントのインストール

**ビルドステップの追加** $\to$ **シェル実行**を選択し、以下のコマンドを入力します。

```bash
echo '===== 0. Docker CLIをインストール ====='
# Debian/UbuntuベースのJenkinsコンテナの場合:
# aptパッケージリストを更新し、Docker CLIをインストール
apt-get update -y && apt-get install -y docker.io

# ※ もし上記のコマンドでエラーが出る場合（コンテナがAlpineベースの場合など）、
#    代わりに以下のコマンドを試してください:
# apk update && apk add docker
```

#### ステップ 1: Build (Mavenツールによるビルド)

**ビルドステップの追加** $\to$ **Invoke top-level Maven targets** を選択し、以下の設定を行います。

| 設定項目 | UI上の日本語名 | 内容 |
| :--- | :--- | :--- |
| **Maven Version** | Mavenバージョン | **`M3_HOME`** (1.2節で設定した名前) を選択 |
| **Goals** | ゴール | `clean package -DskipTests` |

**【ビルド後のアクション（JARパス取得）】**

**「ビルド後の処理の追加」** $\to$ **「シェル実行」** を追加し、以下のコマンドを入力してJARパスを取得します。

```bash
echo '===== 1. Maven Build - Post Action: Get JAR Path ====='
# JARファイルのパスを取得し、以降のステップで使用するため一時ファイルに保存
JAR_PATH=$(find target -name 'restful-*.jar' | head -n 1)
echo $JAR_PATH > jar_path.tmp
```

#### ステップ 2: Deploy (デプロイ)

**ビルドステップの追加** $\to$ **シェル実行**を選択し、以下のコマンドを入力します。

```bash
echo '===== 2. Deploy and Restart Service ====='

# JARファイルのパスを読み込み
JAR_PATH=$(cat jar_path.tmp)

# find コマンドでファイルパスを動的に取得
PROP_FILE="application-stg.properties"
LOG_FILE="logback-spring.xml"

PROP_PATH=$(find . -name "${PROP_FILE}" -print -quit | head -n 1)
LOG_PATH=$(find . -name "${LOG_FILE}" -print -quit | head -n 1)

# ファイルが見つからない場合はエラーを出す
if [ -z "${PROP_PATH}" ] || [ -z "${LOG_PATH}" ]; then
    echo "ERROR: Configuration file not found. Check if ${PROP_FILE} and ${LOG_FILE} exist in the workspace."
    exit 1
fi

# 1. ファイルをstg-serverコンテナにコピー
docker cp ${JAR_PATH} stg-server:/opt/api-service/app.jar
docker cp ${PROP_PATH} stg-server:/opt/api-service/application-stg.properties
docker cp ${LOG_PATH} stg-server:/opt/api-service/logback-spring-stg.xml

# 2. サービスの再起動はコンテナに任せるため、明示的な kill コマンドは削除
echo "Deployment successful. The service will start automatically inside the container."
```

-----

## 3\. Jenkinsジョブの実行

### 3.1 ジョブの実行

1.  ジョブの設定を「**保存**」します。
2.  ジョブページに戻り、左メニューの「**ビルド実行**」をクリックします。
3.  ビルド番号をクリックし、「**Console Output**」（コンソール出力）を選択することで、ログを確認します。

### 3.2 初回デプロイ後の対応（手動マイグレーション）

ジョブが成功した後、**手動**でマイグレーションを実施してください。

#### A) スキーマの初期化（クリーンアップ）

**警告**: このコマンドは、データベース内の**すべてのテーブルとデータ**を削除します。初期化時のみ実行してください。

```bash
# データベース全体を初期化（cleanDisabledを無効化する必要がある）
docker run --rm \
  --network staging_default \
  -v "$(pwd)/migration:/flyway/sql" \
  flyway/flyway:10.10.0-alpine \ 
  -url="jdbc:postgresql://db:5432/your_dev_db" \
  -user="your_user" \
  -password="your_password" \
  -cleanDisabled=false \ 
  clean
```

#### B) マイグレーションの実行

クリーンアップ後、または初回デプロイ後にマイグレーションを実行します。

```bash
# Flyway CLI コンテナを使用してマイグレーションを実行
docker run --rm \
  --network staging_default \
  -v "$(pwd)/migration:/flyway/sql" \
  flyway/flyway:10.10.0-alpine \ 
  -url="jdbc:postgresql://db:5432/your_dev_db" \
  -user="your_user" \
  -password="your_password" \
  migrate
```
