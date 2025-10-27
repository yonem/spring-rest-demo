# Rocky Linux 9の基本イメージを使用
FROM rockylinux:9

# 必要なパッケージのインストール (git, sudo, curl, unzip, wget, procps)
RUN dnf update -y && \
    dnf install -y git sudo which curl unzip wget procps && \
    dnf clean all

# apiuser の作成とsudo権限の付与 (パスワードなしでsudo可能)
RUN useradd -ms /bin/bash apiuser && \
    echo 'apiuser ALL=(ALL) NOPASSWD: ALL' >> /etc/sudoers

# サービスファイル、JARファイル配置ディレクトリの作成と権限設定
RUN mkdir -p /opt/api-service && \
    chown -R apiuser:apiuser /opt/api-service

# SDKMAN! のインストール (apiuser で実行できるように設定)
USER apiuser
WORKDIR /home/apiuser
RUN curl -s "https://get.sdkman.io" | bash

# SDKMAN!の初期化とJava (Temurin 21)のインストール
SHELL ["/bin/bash", "-c"]
RUN echo "sdkman_auto_answer=true" > /home/apiuser/.sdkman/etc/config && \
    source "$HOME/.sdkman/bin/sdkman-init.sh" && \
    sdk install java 21.0.2-tem

# サービス起動用スクリプトの配置
USER root
COPY start-service.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/start-service.sh

# デフォルトのコマンド: サービス起動スクリプトを実行
CMD ["/usr/local/bin/start-service.sh"]
