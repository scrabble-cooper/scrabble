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



CREATE TABLE IF NOT EXISTS games (
    game_id bigint NOT NULL DEFAULT nextval('game_seq'),
	p1_id bigint NOT NULL,
	p2_id bigint DEFAULT NULL,  -- can be null because first play starts game and then other can scan for open games and then join
    players_turn_id bigint DEFAULT NULL, -- whos turn is it. Always the players id ie p1_id or p2_id
    passcount int NOT NULL DEFAULT 0, -- game ends in a draw if there are three passess in a row
    start_ts timestamp DEFAULT NOW (),
    last_update_ts timestamp DEFAULT NOW(),
	p1_score int NOT NULL DEFAULT 0,
	p2_score int NOT NULL DEFAULT 0,
	p1_hand varchar(7) NOT NULL DEFAULT ''::text , --- can be empty if player uses every letter in end game
	p2_hand varchar(7) NOT NULL DEFAULT ''::text ,
	current_round int NOT NULL DEFAULT 0,
	letters_left varchar(100) NOT NULL DEFAULT 'AAAAAAAAABBCCDDDDEEEEEEEEEEEEFFGGGHHIIIIIIIIIJKLLLLMMNNNNNNOOOOOOOOPPQRRRRRRSSSSTTTTTTUUUUVVWWXYYZ  '::text, --- Do not forget the blanks
	winner int DEFAULT 0,
 	board    character (225) NOT NULL DEFAULT ' '::character , -- postgres will pad it with spaces to 225
 	lastplay character (225) NOT NULL DEFAULT ' '::character , --- stores the last move played. allowing it to be removed easily if the other play challenges it

	PRIMARY KEY (game_id),
	FOREIGN KEY (p1_id) REFERENCES users(user_id) ON DELETE CASCADE,
	FOREIGN KEY (p2_id) REFERENCES users(user_id) ON DELETE CASCADE,
	-- CHECK (p1_id <> p2_id),
    --  why not you can play yourself
	CHECK (winner >=0 AND winner <= 3)
  -- always with respect to p1  0 : incomplete  1 : wins   2 : lost  3 : draw  4 : abandon

);





CREATE TABLE IF NOT EXISTS words (
        definition text DEFAULT ''::text NOT NULL,
        length integer DEFAULT 0 NOT NULL,
        numofa integer DEFAULT 0,
        numofb integer DEFAULT 0,
        numofc integer DEFAULT 0,
        numofd integer DEFAULT 0,
        numofe integer DEFAULT 0,
        numoff integer DEFAULT 0,
        numofg integer DEFAULT 0,
        numofh integer DEFAULT 0,
        numofi integer DEFAULT 0,
        numofj integer DEFAULT 0,
        numofk integer DEFAULT 0,
        numofl integer DEFAULT 0,
        numofm integer DEFAULT 0,
        numofn integer DEFAULT 0,
        numofo integer DEFAULT 0,
        numofp integer DEFAULT 0,
        numofq integer DEFAULT 0,
        numofr integer DEFAULT 0,
        numofs integer DEFAULT 0,
        numoft integer DEFAULT 0,
        numofu integer DEFAULT 0,
        numofv integer DEFAULT 0,
        numofw integer DEFAULT 0,
        numofx integer DEFAULT 0,
        numofy integer DEFAULT 0,
        numofz integer DEFAULT 0,
        word text NOT NULL
);
