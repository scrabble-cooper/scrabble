CREATE SEQUENCE user_seq start with 1;

CREATE TABLE user (
  user_id bigint NOT NULL DEFAULT nextval('user_seq'),
  user_name varchar(50) NOT NULL UNIQUE,
  password varchar(50) NOT NULL,
  total_games DEFAULT int 0,
  total_win DEFAULT int 0,
  total_loss DEFAULT int 0,
  total_ties DEFAULT int 0,
  PRIMARY KEY (user_id) --forces user_id to be unique
);
