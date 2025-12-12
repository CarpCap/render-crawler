FROM selenium/standalone-chrome:4.38.0-20251101
LABEL org.opencontainers.image.authors="CarpCap"
ENV TZ=Asia/Shanghai
COPY build/render-crawler /home/render-crawler
COPY config.toml /home/config/config.toml
USER root
RUN chmod +x /home/render-crawler
COPY app.conf /etc/supervisor/conf.d/app.conf
