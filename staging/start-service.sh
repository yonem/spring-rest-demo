#!/bin/bash
# start-service.sh - コンテナ環境のベストプラクティスに準拠

# サービス停止時のクリーンアップ (Ctrl+CやSIGTERMを受信した場合)
cleanup() {
    echo "Stopping API Service gracefully..."
    # killコマンドは不要。フォアグラウンドプロセスがSIGTERMを受け取り終了するのを待つ。
    exit 0
}

# Ctrl+CやSIGTERMでクリーンアップを実行
trap cleanup SIGINT SIGTERM

# アプリケーションが存在しない場合の警告
if [ ! -f /opt/api-service/app.jar ]; then
    echo "WARNING: /opt/api-service/app.jar not found. Waiting for deployment..."
    # ファイルがない場合はサービスを起動せず、コンテナを待機状態にする
    while [ ! -f /opt/api-service/app.jar ]; do
        sleep 5
    done
    echo "Deployment detected. Starting service..."
fi

# アプリケーションをフォアグラウンドで実行 (ベストプラクティス)
# ログは標準出力 (stdout) に直接出力されます。
echo "Starting API Service in foreground..."
# apiuserとしてアプリケーションを実行
/usr/bin/sudo -u apiuser sh -c "
source /home/apiuser/.sdkman/bin/sdkman-init.sh && \
    exec java -Dlogging.config=file:/opt/api-service/logback-spring-stg.xml \
    -jar /opt/api-service/app.jar \
    --spring.profiles.active=stg \
    --spring.config.location=file:/opt/api-service/application-stg.properties
"

# execで実行した場合、この後の行は到達しない
echo "API Service terminated."