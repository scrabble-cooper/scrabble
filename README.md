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
```

```
psql -h localhost -U postgres -d scrabble -f users.sql
psql -h localhost -U postgres -d scrabble -f test_users.sql
```
