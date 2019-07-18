# spring-hotel

**Steps to build and run docker image**:

1\. Clone the repository

```git clone https://github.com/vahundos/spring-hotel.git```

2\. Move to project folder

```cd spring-hotel```

3\. Build application jar file

```
mvn clean package
```

4\. Build database docker image

```
docker build -f src/main/docker/db/Dockerfile -t db .
``` 

5\. Build web application docker image

```
docker build -f src/main/docker/web/Dockerfile -t web .
```

6\. Run docker container from db image

```
docker run -d --name db db:latest
```

7\. Run docker container from web image linked to already run db container

```
docker run -p 8080:8080 --name web --link db  web:latest
```