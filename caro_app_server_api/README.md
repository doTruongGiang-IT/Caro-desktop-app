cd caro_app_server_api
make build
application.properties
make create_network
docker-compose up

cd caro_app_server_socket
make build
APIConnection.java
create socker admin acc
USE caro_db;
INSERT INTO users(day_of_birth, first_name, gender, is_active, last_name, password, role, score, username)
VALUES ("23/11/2000", "Socket", "undefined", 1, "Account", "d6cbe3df6eea3b7381b6e09f11822e28d0c1077ba965420f5ecf9a306b01a4cd9e8c45045fb916af", "admin", 0, "socket@caro.com");

docker-compose up
