#!/bin/sh

GIT_HOSTS="github.com" 

JENKINS_HOME="/var/jenkins_home"
SSH_DIR="$JENKINS_HOME/.ssh"
PRIVATE_KEY="$SSH_DIR/id_ed25519"
ROOT_SSH_DIR="/root/.ssh"

echo "Checking for existing SSH key..."

if [ ! -f "$PRIVATE_KEY" ]; then
    echo "SSH key not found. Generating new ed25519 key pair..."
    
    # ----------------------------------------------------
    # 1. 秘密鍵の生成とPEM形式への変換（jenkinsユーザーホーム）
    # ----------------------------------------------------
    mkdir -p "$SSH_DIR"
    
    # キーをED25519形式で生成
    ssh-keygen -t ed25519 -f "$PRIVATE_KEY" -N "" -C "jenkins@ci-auto-generated"
    
    # 互換性の高いPEM形式に変換し、libcryptoエラーを解消
    echo "Converting private key to PEM format for Jenkins compatibility..."
    ssh-keygen -p -m PEM -N "" -f "$PRIVATE_KEY"
    
    # ----------------------------------------------------
    # 2. ホスト鍵の登録（jenkinsユーザーとrootユーザーの両方）
    # ----------------------------------------------------
    echo "Registering Git Host Keys: $GIT_HOSTS"
    
    # ホスト鍵を known_hosts に登録する関数
    register_host_key() {
        TARGET_DIR=$1
        TARGET_HOST=$2
        
        mkdir -p "$TARGET_DIR"
        
        # ssh-keyscanで鍵を取得し、known_hostsに追記
        ssh-keyscan -H "$TARGET_HOST" >> "$TARGET_DIR/known_hosts" 2>/dev/null
        ssh-keyscan -H -p 443 "$TARGET_HOST" >> "$TARGET_DIR/known_hosts" 2>/dev/null
        chmod 600 "$TARGET_DIR/known_hosts"
        echo "  - Registered key for $TARGET_HOST in $TARGET_DIR"
    }

    # 定義されたホストすべてに対して登録を実行
    for HOST in $GIT_HOSTS; do
        # jenkinsユーザーの known_hosts に登録
        register_host_key "$SSH_DIR" "$HOST"
        
        # rootユーザーの known_hosts に登録 (ジョブ設定のバリデーション用)
        register_host_key "$ROOT_SSH_DIR" "$HOST"
    done
    
    # ----------------------------------------------------
    # 3. パーミッションとオーナーシップの設定
    # ----------------------------------------------------
    chmod 600 "$PRIVATE_KEY"
    # jenkinsユーザーのファイルとディレクトリのオーナーをjenkinsに設定
    chown -R jenkins:jenkins "$SSH_DIR"
    
    echo "SSH Key setup completed."
    echo "----------------------------------------------"
    echo "🔑 PUBLIC KEY (REGISTER THIS TO GIT SERVICE) 🔑"
    cat "$PRIVATE_KEY.pub"
    echo "----------------------------------------------"
else
    echo "SSH key already exists. Skipping generation."
fi

# 既存のCMDを実行 (Jenkins本体を起動)
exec "$@"