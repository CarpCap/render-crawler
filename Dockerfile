from selenium/standalone-chrome
COPY target/selenium-crawler-0.0.1-SNAPSHOT.jar /app.jar
COPY ./app.conf /etc/supervisor/conf.d/app.conf


