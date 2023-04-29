CREATE SEQUENCE game_seq start with 1; -- then next value will be 1

CREATE TABLE IF NOT EXISTS games (
    game_id bigint NOT NULL DEFAULT nextval('game_seq'),
	p1_id bigint NOT NULL,
	p2_id bigint DEFAULT NULL,  -- can be null because first play starts game and then other can scan for open games and then join
	p1_username varchar DEFAULT '', -- when a user creates a game add user_name as well as user id otherwise future selects will need to access both games and users tables to get the name
	p2_username  varchar DEFAULT '', -- when a user joins  a game add user_name as well as user id otherwise future selects will need to access both games and users tables to get the name
    players_turn_id bigint DEFAULT NULL, -- whos turn is it. Always the players id ie p1_id or p2_id
    passcount int NOT NULL DEFAULT 0, -- game ends in a draw if there are three passess in a row
    start_ts timestamp DEFAULT NOW (),
    last_update_ts timestamp DEFAULT NOW(),
	p1_score int NOT NULL DEFAULT 0,
	p2_score int NOT NULL DEFAULT 0,
	p1_hand character(9) NOT NULL DEFAULT ' '::character , --- will always be 9 characters postgres will pad with spaces
	p2_hand character(9) NOT NULL DEFAULT ' '::character , --- racks have nine spaces so to avoid conversion save the rack not the letters
	current_round int NOT NULL DEFAULT 0,
	letters_left varchar(100) NOT NULL DEFAULT 'AAAAAAAAABBCCDDDDEEEEEEEEEEEEFFGGGHHIIIIIIIIIJKLLLLMMNNNNNNOOOOOOOOPPQRRRRRRSSSSTTTTTTUUUUVVWWXYYZ**'::text, --- Do not forget the blanks
	winner int DEFAULT 0,
	challengecount int DEFAULT 0,
 	board    character (225) NOT NULL DEFAULT ' '::character , -- postgres will pad it with spaces to 225
 	lastplay character (225) NOT NULL DEFAULT ' '::character , --- stores the last move played. allowing it to be removed easily if the other play challenges it
 	playedby character (225) NOT NULL DEFAULT ' '::character , --- stores which player p_1 or p_2 played the letter 1 or 2

	PRIMARY KEY (game_id),
	FOREIGN KEY (p1_id) REFERENCES users(user_id) ON DELETE CASCADE,
	FOREIGN KEY (p2_id) REFERENCES users(user_id) ON DELETE CASCADE,
	CHECK (p1_id <> p2_id),
	CHECK (winner >=0 AND winner <= 4)
  -- always with respect to p1  0 : incomplete  1 : wins   2 : lost  3 : draw  4 : abandon

);


