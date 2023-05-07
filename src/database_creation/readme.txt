db docker must be running

database scrabble must exist and be empty if not 

in psql type 

CREATE DATABASE "scrabble" WITH OWNER "postgres" ENCODING 'UTF8' ; 

may need CREATE DATABASE "scrabble" WITH OWNER "postgres" ENCODING 'UTF8' LC_COLLATE = 'en-US' LC_CTYPE = 'en-US' TEMPLATE template0;

The database exists now in a terminal window

cd to <db_Files> // 127.0.0.1 works below because by default postgres is listenning on all addresses ie 0.0.0.0 

psql -h 127.0.0.1 -d scrabble -U postgres < table_users.sql 

psql -h 127.0.0.1 -d scrabble -U postgres < table_games.sql 

psql -h 127.0.0.1 -d scrabble -U postgres < table_words.sql 

psql -h 127.0.0.1 -d scrabble -U postgres < word_data.sql 

