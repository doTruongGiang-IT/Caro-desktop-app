
install docker
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

INSERT INTO users(day_of_birth, first_name, gender, is_active, last_name, password, role, score, username)
VALUES ("23/11/2000", "Giang", "undefined", 1, "Account", "0ea1024482845aab95ff8a996076d54b6f63fbd91f51d5900f16fffb193c07231365e106197c395b", "user", 0, "giang@caro.com");


INSERT INTO users(day_of_birth, first_name, gender, is_active, last_name, password, role, score, username)
VALUES ("23/11/2000", "Dat", "undefined", 1, "Account", "0ea1024482845aab95ff8a996076d54b6f63fbd91f51d5900f16fffb193c07231365e106197c395b", "user", 0, "dat@caro.com");


INSERT INTO users(day_of_birth, first_name, gender, is_active, last_name, password, role, score, username)
VALUES ("23/11/2000", "Doanh", "undefined", 1, "Account", "0ea1024482845aab95ff8a996076d54b6f63fbd91f51d5900f16fffb193c07231365e106197c395b", "user", 0, "doanh@caro.com");


INSERT INTO users(day_of_birth, first_name, gender, is_active, last_name, password, role, score, username)
VALUES ("23/11/2000", "Dung", "undefined", 1, "Account", "0ea1024482845aab95ff8a996076d54b6f63fbd91f51d5900f16fffb193c07231365e106197c395b", "user", 0, "dung@caro.com");


INSERT INTO users(day_of_birth, first_name, gender, is_active, last_name, password, role, score, username)
VALUES ("23/11/2000", "Thien", "undefined", 1, "Account", "0ea1024482845aab95ff8a996076d54b6f63fbd91f51d5900f16fffb193c07231365e106197c395b", "user", 0, "thien@caro.com");

docker-compose up
