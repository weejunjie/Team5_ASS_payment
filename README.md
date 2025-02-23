#Docker
##build image mysql
1. docker network create rentify (Create docker network if not yet created)
2. docker build -t rentify_mysql_image -f Dockerfile.mysql . 
3. docker run -d --name rentify_mysql_container --network=rentify -p 3306:3306 rentify_mysql_image 
4. docker start rentify_mysql_container 
5. docker exec -it rentify_mysql_container mysql -uroot -p 
6. Note: password = default1111

##build image java
1. docker network create rentify (Create docker network if not yet created)
2. docker build -t payment_microservice -f Dockerfile.payment .
3. docker rm -f payment_microservice || true && docker run -d -e DOCKER_CONTAINER=true --name payment_microservice --network=rentify -p 8080:8080 payment_microservice