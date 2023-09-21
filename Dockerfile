FROM openjdk:17-alpine
WORKDIR /app
COPY target/employee-service-1.0-SNAPSHOT.jar /app/
ENV DB_HOST=employees-dev-db\
    DB_PORT=5432 \
    DB_DATABASE=employees
CMD java -jar employee-service-1.0-SNAPSHOT.jar
