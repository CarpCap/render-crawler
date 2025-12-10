FROM selenium/standalone-chrome:4.38.0-20251101
COPY target/render-crawler-0.0.1-SNAPSHOT.jar /render-crawler.jar
COPY app.conf /etc/supervisor/conf.d/app.conf


