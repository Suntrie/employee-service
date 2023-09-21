# employee-service

Employee-service is dedicated to store and control information about employees.

To start employee-service from IDE the next steps should be proceeded:

1. Start docker-compose.yml from the root folder. It will start container with Postgres database, Zookeeper service and Kafka broker.
2. Start main class EmployeesApplication with use of IDE tools; 

To start it as a container you also could follow the next steps:

1. Build application .jar (with use of Maven package, by default this stage also includes tests execution);
2. Execute docker-compose.yml from the root folder. It will at first create start container with Postgres database, Zookeeper service and Kafka broker.

After the start you could use swagger endpoints to access service functionality.
Swagger endpoints are available as http://localhost:8080/swagger-ui/index.html.

To access methods of the resource controller EmployeeController you should use endpoint /api/users/signin to obtain JWT token.

2 predefined users are added with credentials:

* admin@mail 55 - ADMIN role (modification & read operations)
* user@mail 55 - USER role (just read operations)

If other users are required, they can be added manually into the database
(tables employees.user and employees.user_role).

Integration tests and some unit tests also could be found in src/test/java/integration
and src/test/java/unit folders respectively. 