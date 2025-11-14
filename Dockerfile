from selenium/standalone-chrome
COPY target/selenium-crawler-1.0.0-SNAPSHOT.jar /app.jar
COPY ./app.conf /etc/supervisor/conf.d/app.conf


