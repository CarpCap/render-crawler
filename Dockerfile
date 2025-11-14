FROM selenium/standalone-chrome:106.0-20221024
COPY target/render-crawler-0.0.1-SNAPSHOT.jar /app.jar
COPY ./app.conf /etc/supervisor/conf.d/app.conf


