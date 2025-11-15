// --- 設定 ---
// Spring Boot APIのヘルスチェックエンドポイントURLを指定
const API_HEALTH_URL = 'http://localhost:8080/actuator/health';
// ----------------

const healthStatusElement = document.getElementById('health-status');
const detailsElement = document.getElementById('response-details');
const checkButton = document.getElementById('check-button');

/**
 * ヘルスチェックAPIを呼び出す関数
 */
async function checkApiHealth() {
    // 画面表示を初期化（チェック中に戻す）
    healthStatusElement.textContent = 'チェック中...';
    healthStatusElement.className = 'loading';
    detailsElement.textContent = 'リクエスト送信中...';
    checkButton.disabled = true;

    try {
        const response = await fetch(API_HEALTH_URL);
        const data = await response.json();

        // 詳細情報を表示
        detailsElement.textContent = JSON.stringify(data, null, 2);

        // ステータスコードが200番台、かつJSONデータに 'status': 'UP' が含まれるか確認
        if (response.ok && data.status === 'UP') {
            healthStatusElement.textContent = '稼働中 (UP)';
            healthStatusElement.className = 'up';
        } else {
            // ステータスコードがOKでも、内部ステータスがDOWNの場合
            healthStatusElement.textContent = `停止中 (${data.status || 'DOWN'})`;
            healthStatusElement.className = 'down';
        }

    } catch (error) {
        // ネットワークエラー、CORSエラー、APIが完全に停止している場合など
        healthStatusElement.textContent = '接続エラー';
        healthStatusElement.className = 'error';
        detailsElement.textContent = `APIへの接続に失敗しました。\n\n詳細:\n${error.message}\n\n[ヒント] Spring Bootアプリが起動しているか、CORS設定が許可されているか確認してください。`;
    } finally {
        checkButton.disabled = false;
    }
}

// ページ読み込み完了時に自動でチェックを実行
window.onload = checkApiHealth;

// ボタンクリック時にチェックを実行
checkButton.addEventListener('click', checkApiHealth);