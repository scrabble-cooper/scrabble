# Database Dockerfile
FROM postgres

# Copy the SQL files to the container
COPY ./init.sql /docker-entrypoint-initdb.d/0_init.sql
COPY ./table_users.sql /docker-entrypoint-initdb.d/1_table_users.sql
COPY ./table_games.sql /docker-entrypoint-initdb.d/2_table_games.sql
COPY ./table_words.sql /docker-entrypoint-initdb.d/3_table_words.sql
