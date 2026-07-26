FROM maven:3.9.9-eclipse-temurin-17

#ARG test_profile=api
#ARG API_BASEURL=http://backend:4111
#ARG UI_BASEURL=http://frontend
#ARG DB_URL=jdbc:postgresql://postgres:5432/nbank
#ARG WIRE_MOCK_HOST=wiremock;
#ARG WIRE_MOCK_PORT=8080;
#Для чего тут указываю ARG?

ENV test_profile=all
ENV api_baseurl=http://backend:4111
ENV ui_baseurl=http://frontend
ENV remote_host=http://selenoid:4444/wd/hub
ENV db_url=jdbc:postgresql://postgres:5432/nbank
ENV wire_mock_host=wiremock
ENV wire_mock_port=8080

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY . .

USER root

CMD /bin/bash -c " \
    mkdir -p /app/logs ; \
    { \
    echo '>>> Running tests with profile: ${test_profile}' ; \
    mvn test -q -P ${test_profile} ; \
    \
    echo '>>> Running surefire-report:report' ; \
    mvn -DskipTests=true surefire-report:report ; \
   } > /app/logs/run.log 2>&1"

