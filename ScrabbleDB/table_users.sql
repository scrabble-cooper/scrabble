CREATE SEQUENCE users_seq start with 1;

CREATE TABLE IF NOT EXISTS users (
      user_id bigint NOT NULL DEFAULT nextval('users_seq'),
      user_name varchar(50) NOT NULL UNIQUE,
      password varchar(50) NOT NULL,
      total_games int NOT NULL DEFAULT 0,
      total_win int NOT NULL DEFAULT 0,
      total_loss int NOT NULL DEFAULT 0,
      total_ties int NOT NULL DEFAULT 0,
      PRIMARY KEY (user_id) --forces user_id to be unique
);


