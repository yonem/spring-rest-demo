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
