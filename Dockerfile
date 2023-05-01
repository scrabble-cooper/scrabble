#FROM maven:3.8.7-eclipse-temurin-19 AS build
#ADD src /project
#WORKDIR /project
#RUN #mvn -e package

#FROM eclipse-temurin:latest
#COPY --from=build /project/target/scrabble-0.0.1-SNAPSHOT-spring-boot.jar /app/scrabble.jar
#ENTRYPOINT ["java","-jar","/app/scrabble.jar"]

FROM postgres

# Copy the SQL files to the container
COPY ScrabbleDB/init.sql /docker-entrypoint-initdb.d/0_init.sql
COPY ScrabbleDB/table_users.sql /docker-entrypoint-initdb.d/1_table_users.sql
COPY ScrabbleDB/table_games.sql /docker-entrypoint-initdb.d/2_table_games.sql
COPY ScrabbleDB/table_words.sql /docker-entrypoint-initdb.d/3_table_words.sql