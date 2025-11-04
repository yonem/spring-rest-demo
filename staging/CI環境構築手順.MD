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

### 1.3 SSHキーを使用したGit認証の設定

JenkinsがSSHプロトコルでGitリポジトリからプルできるように、自動生成されたキーペアをJenkinsに登録します。

#### 1.3.1 JenkinsサーバーのSSHキーペア生成（自動化済み）

**注意**: この手順は、Docker Composeのカスタム設定（`Dockerfile.jenkins`および`generate-ssh-key.sh`）により、**`docker compose up`実行時に自動で完了**しています。手動でのキー生成は不要です。

**公開鍵の取得**:
コンテナの起動ログ、または以下のコマンドで公開鍵の内容を取得し、コピーします。

```bash
docker logs jenkins-server
# または
docker exec jenkins-server cat /var/jenkins_home/.ssh/id_ed25519.pub
```

**秘密鍵の場所**:
秘密鍵はコンテナ内の `/var/jenkins_home/.ssh/id_ed25519` に自動で生成され、正しいパーミッションが設定されています。

#### 1.3.2 Gitサービスへの公開鍵登録

1.3.1で取得した公開鍵をGitサービス（GitHubなど）の「Deploy keys」または「SSH keys」に登録します。

#### 1.3.3 JenkinsへのSSH秘密鍵の登録

Jenkinsの資格情報マネージャに、自動生成された秘密鍵を登録します。

1.  「**Security - Credentials**」へ移動し、**global**ドメインで「**Add Credentials**」をクリックします。
2.  以下の情報を登録します。

| フィールド | UI上の日本語名 | 内容 |
| :--- | :--- | :--- |
| **Kind** | 種類 | `SSH Username with private key` を選択 |
| **ID** | ID | **`GIT_SSH_KEY`** (ジョブで使用するID) |
| **Username** | ユーザー名 | **`git`** (Gitサービスが要求するSSHユーザー名) |
| **Private Key** | 秘密鍵 | 「**Enter directly**」を選択し、以下のコマンドで取得した**秘密鍵の内容全体**を貼り付けます。 |

**【秘密鍵の内容を取得するコマンド】**

```bash
docker exec jenkins-server cat /var/jenkins_home/.ssh/id_ed25519
```

👆 このコマンドの出力内容全体（-----BEGIN...から...ENDまで）をコピーしてください。

-----

## 2\. CI/CD ジョブの定義 (フリースタイル・プロジェクト)

### 2.1 新規ジョブの作成

1.  Jenkinsダッシュボードで左メニューの「**新規ジョブ作成**」をクリックします。
2.  **Item Name** に `Staging-Deploy-Freestyle` などの名前を入力し、「**フリースタイル・プロジェクトのビルド**」を選択して「**OK**」をクリックします。

### 2.2 ジョブの設定（General & ソースコード管理項目）

  * **General**: 必要に応じてジョブの説明を記述します。
  * **ソースコード管理**: GitリポジトリURL（**SSH形式**を利用する場合は`git@host:user/repo.git`の形式）とアクセス認証情報（Credentials）を設定します。SSH認証を利用する場合は、1.3節で登録した\*\*`GIT_SSH_KEY`\*\*を選択してください。

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
#    代わりに以下のコマンドを試してください:
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
一時的なマイグレーション用コンテナを起動し、マイグレーションを実行します。

マイグレーション環境変数を設定する。

```bash
# Flyway SQLファイルが格納されているローカルディレクトリのパス
MIGRATION_DIR="migration" 
FLYWAY_IMAGE="flyway/flyway:11.10.0-alpine"

# データベース接続情報（application-stg.propertiesより）
DB_HOST="db"  # docker-compose-stg.yml のサービス名
DB_NAME="your_dev_db"
DB_USER="your_user"
DB_PASS="your_password"
```

**警告**: このコマンドは、データベース内の**すべてのテーブルとデータ**を削除します。初期化時のみ実行してください。

```bash
# Flyway CLI コンテナを使用してクリーンを実行
# データベース全体を初期化
docker run --rm \
  --network staging_default \
  -v "$(pwd)/${MIGRATION_DIR}:/flyway/sql" \
  -e FLYWAY_URL="jdbc:postgresql://${DB_HOST}:5432/${DB_NAME}" \
  -e FLYWAY_USER="${DB_USER}" \
  -e FLYWAY_PASSWORD="${DB_PASS}" \
  -e FLYWAY_CLEAN_DISABLED=false \
  ${FLYWAY_IMAGE} \
  clean
```

クリーンアップ後、または初回デプロイ後にマイグレーションを実行します。

```bash
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

## 4\. 動作確認

サービスが起動し、ログがElasticsearchに転送されていることを確認します。

```bash
# 1. stg-serverのコンテナログでアプリケーションの起動を確認
docker logs -f stg-server

# 2. Fluent Bitログを確認 (Elasticsearchへの転送状況を確認)
docker logs -f fluent-bit
# 「[es: info] flush_mode=async, status=200」のような転送成功を示すログを確認

# 3. Jenkinsログを確認
docker logs -f jenkins-server

# 4. APIエンドポイントへアクセス (ポート8083)
http://localhost:8083/
```

### ログのトレーサビリティ確認 (Kibana)

KibanaのWebインターフェースを通じて、ログが正しく集約されているか確認します。

1. ブラウザで **`http://localhost:5601`** にアクセスします。
2. 左側のメニューから **Analytics** -\> **Discover** を選択します。
3. `java-app-*`のデータビューを作成します。
4. APIアクセスによって発生したログがKibana上で検索・表示できることを確認します。
