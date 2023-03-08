# scrabble

### LOCAL DATABASE

Starting local postgres:
```
docker run --rm --name lil-postgres -e POSTGRES_PASSWORD=password -d -v $HOME/srv/postgres:/var/lib/postgresql/data -p 5432:5432 postgres
```

```psql -h localhost -U postgres``` to open postgres terminal

Creating local database:
```
CREATE DATABASE scrabble;
GRANT ALL PRIVILEGES ON DATABASE scrabble TO postgres;
```
Inside postgres, ```\c scrabble``` to connect to database scrabble

Adding SQL tables/data:
```
psql -h localhost -U postgres -d scrabble -f users.sql
psql -h localhost -U postgres -d scrabble -f test_users.sql
```

### SPRINGBOOT

```
docker compose build
docker compose up
```

Testing functions:
```
 curl -X POST -H "Content-Type: application/json" -d '{"gameID": 12, "charToChange":"RDW", "boardRow":1, "boardCol":1}' http://localhost:8080/updateBoard

 curl -X POST -H "Content-Type: application/json" -d '{"gameID": 12, "letter": "E"}' http://localhost:8080/drawFromHand
 
 curl -X POST -H "Content-Type: application/json" -d '{"gameID": 12, "letter": "E"}' http://localhost:8080/drawIntoHand

 curl -X POST -H "Content-Type: application/json" -d '{"gameID": 12, "points": 2}' http://localhost:8080/updatePoints
 ```
