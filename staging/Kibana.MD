## 📄 Kibanaログ管理・運用手順書

この手順書は、Elasticsearchに集約されたログをKibanaで効率的に検索、可視化、監視するための操作をまとめたものです。

---

## 1. ログ集約の初期設定（Data Viewの作成）

Kibanaでログデータを扱うためには、Elasticsearchのインデックスに対応する**データビュー（旧称: インデックスパターン）**
を最初に作成する必要があります。

### 1.1 KibanaへのアクセスとData Viewの作成

1. ブラウザを開き、KibanaのURL `http://localhost:5601` にアクセスします.
2. Kibana画面左側のナビゲーションメニューを開き、「**Management**」セクション（⚙️）から「**Stack Management**」を選択します.
3. 左側のメニューで「**Kibana**」セクションの「**Data Views**」を選択します.
4. 「**Create data view**」ボタンをクリックします。
5. **Index pattern** (インデックスパターン) に `java-app-*` を入力します。これは、Fluent
   BitがElasticsearchに書き込むインデックス名に対応しています。
6. **Time field** (時間フィールド) に `@timestamp` を選択します。
7. **Create data view** をクリックして完了します。一覧に `java-app-*` が存在し、ステータスが「Ready」であることを確認します。

---

## 2. ログの検索とフィルタリング方法 (Discover)

「Discover」は、生ログデータの検索とフィルタリングを行う中心的な機能です。

### 2.1 基本操作

1. Kibana画面左側のナビゲーションメニューで、「**Analytics**」セクションの「**Discover**」を選択します。
2. 画面上部のドロップダウンで、作成したデータビュー **`java-app-*`** が選択されていることを確認します。
3. 画面右上にある「**Time range**」セレクタで検索期間を指定します（例: `Last 1 hour`, `Absolute`）。

### 2.2 検索クエリ（KQL: Kibana Query Language）

画面上部の検索バーに**KQL**を使用して、特定のログに絞り込みます。

| 検索目的            | KQLクエリの例                                     | 説明                                        |
|:----------------|:---------------------------------------------|:------------------------------------------|
| **ログレベル**       | `level: ERROR`                               | `ERROR`レベルのログのみを抽出します。                    |
| **期間内の特定メッセージ** | `message: "*Database connection failed*"`    | ログメッセージ本文に特定の文字列を含むものを検索します（`*`はワイルドカード）。 |
| **複合条件**        | `level: WARN and container_name: stg-server` | `stg-server`コンテナの`WARN`レベルログのみを抽出します。     |
| **フィールド値の除外**   | `NOT level: DEBUG`                           | `DEBUG`レベル以外のすべてのログを表示します。                |

### 2.3 フィールドによる絞り込み

画面左側の「**Available fields**」から、目的のフィールドをクリックすると、そのフィールドの分布（Top 5
Values）が表示されます。特定の値をクリックすることで、その値を含むログのみを即座にフィルタリングできます。

---

## 3. ログ監視とアラート（通知）の作成

Kibanaのアラート機能（Elastic Stackでは「Stack Monitoring」の一部）を使用して、特定の異常なログパターンが発生した場合にメール通知などを設定できます。

### 3.1 メール通知先（コネクタ）の設定

通知アクションを実行するために、通知先となるコネクタを設定します。

1. **Stack Management** > **Alerting** > **Rules and Connectors** に移動します。
2. **Connectors**タブで **Create connector** をクリックし、タイプに **Email** を選択します。
3. **SMTP host/port**：メール送信に使用するSMTPサーバー情報を設定します。検証環境であれば、`host.docker.internal:1025`
   （MailpitのSMTPポート）などを指定できます。
4. **From address**：送信元アドレスを設定し、保存します。

### 3.2 アラート条件（ルールの作成）

ログデータの件数や頻度に基づいたアラートを設定します。

1. **Rules**タブで **Create rule** をクリックし、**Rule type** に **Index threshold** (インデックスの閾値) を選択します。
2. **Rule name**: `High API Error Rate` (任意の名前) を設定します。
3. **Index pattern**: `java-app-*` を指定します。
4. **Conditions**:
    * **When**: `Count` (件数)
    * **is above**: `5` (例: 5件より多い場合)
    * **in the last**: `5 minutes` (例: 過去5分間で)
    * **Optional - Filter query**: `level: ERROR`
5. **Actions**:
    * **Action type** で **Email** を選択し、**Connector** に 3.1で作成したメールコネクタを指定します。
    * **To/Subject/Body**：通知の詳細を設定し、保存します。

| アラート条件の例      | 目的                            | 設定するFilter queryの例                            |
|:--------------|:------------------------------|:----------------------------------------------|
| **エラー頻度の監視**  | 一定時間内の`ERROR`ログ急増を検知。         | `level: ERROR`                                |
| **不正トラフィック**  | 認証失敗ログの急増（ブルートフォース攻撃など）。      | `message: "*Authentication failed*"`          |
| **特定サービスの停止** | 異常終了を示すログ（`terminated`など）を検知。 | `container_name: stg-server AND level: FATAL` |

---

## 4. 設定情報のインポート・エクスポート（Saved Objects）

データビュー、アラート、ダッシュボードなどのKibana設定を環境間で移行したり、バックアップしたりするための機能です。

### 4.1 エクスポート（バックアップ）

1. **Stack Management** > **Kibana** > **Saved Objects** に移動します。
2. エクスポートしたいオブジェクト（データビュー、ルール、コネクタなど）を選択し、画面上部の **Export** ボタンをクリックします。
3. 設定内容がJSON形式のファイル（例: `kibana-export.json`）としてダウンロードされます。

### 4.2 インポート（復元）

1. **Stack Management** > **Kibana** > **Saved Objects** に移動します。
2. 画面上部の **Import** ボタンをクリックします。
3. エクスポートしたJSONファイルを選択し、必要に応じて「Overwrite existing objects」にチェックを入れてインポートを実行します。

---

## 5. その他の便利な使用方法

### 5.1 Visualization と Dashboard の活用

**Visualization (可視化)**
機能を使って、エラーの時系列推移やログレベルの割合などをグラフ化できます。複数のVisualizationを組み合わせて**Dashboard (
ダッシュボード)**を作成することで、システムの健全性を一目で把握できる監視画面を構築できます。

### 5.2 構造化ログ（JSONログ）の利用

Spring BootログがJSON形式で出力されている場合、Kibanaは各ログメッセージ内のフィールド（例：`trace_id`、`response_time_ms`
）を個別のフィールドとして自動で認識します。これにより、以下の高度な分析が可能になります。

* **トレース機能**: 特定の`trace_id`で検索することで、一連のリクエスト処理のログすべてを一瞬で追跡できます。
* **パフォーマンス分析**: `response_time_ms`フィールドを集計し、APIの平均応答時間や95パーセンタイル値などをダッシュボードで監視できます。