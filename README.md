# scrabble

## NOTE TO SELF: important commands

```psql -h localhost -U postgres``` to open postgres terminal

```
CREATE DATABASE scrabble;
GRANT ALL PRIVILEGES ON DATABASE scrabble TO postgres;
```
Inside postgres, ```\c scrabble``` to connect to database scrabble

Starting local postgres:
```
docker run --rm --name lil-postgres -e POSTGRES_PASSWORD=password -d -v $HOME/srv/postgres:/var/lib/postgresql/data -p 5432:5432 postgres
docker run -e POSTGRES_HOST_AUTH_METHOD=md5 -e POSTGRES_PASSWORD=password -p 5432:5432 --name lil-postgres -d postgres
```

```
psql -h localhost -U postgres -d scrabble -f database/sql/users.sql
psql -h localhost -U postgres -d scrabble -f database/sql/test_users.sql
```
