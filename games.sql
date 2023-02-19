CREATE SEQUENCE game_seq start with 1;

CREATE TABLE games (
  game_id bigint NOT NULL DEFAULT nextval('game_seq'),
	p1_id bigint NOT NULL,
	p2_id bigint NOT NULL,
	p1_score int NOT NULL DEFAULT 0,
	p2_score int NOT NULL DEFAULT 0,
	p1_hand varchar(7), -- can be null if player uses every letter in end game
	p2_hand varchar(7),
	current_round int NOT NULL DEFAULT 0,
	letters_left varchar(100) DEFAULT
	 'AAAAAAAAABBCCDDDDEEEEEEEEEEEEFFGGGHHIIIIIIIIIJKLLLLMMNNNNNNOOOOOOOOPPQRRRRRRSSSSTTTTTTUUUUVVWWXYYZ',
	winner int DEFAULT 0,
	board json DEFAULT CAST('{'
      '{TW,NM,NM,DL,NM,NM,NM,TW,NM,NM,NM,DL,NM,NM,TW},'
      '{NM,DW,NM,NM,NM,TL,NM,NM,NM,TL,NM,NM,NM,DW,NM},'
      '{NM,NM,DW,NM,NM,NM,DL,NM,DL,NM,NM,NM,DW,NM,NM},'
      '{DL,NM,NM,DW,NM,NM,NM,DL,NM,NM,NM,DW,NM,NM,DL},'
      '{NM,NM,NM,NM,DW,NM,NM,NM,NM,NM,DW,NM,NM,NM,NM},'
      '{NM,TL,NM,NM,NM,TL,NM,NM,NM,TL,NM,NM,NM,TL,NM},'
      '{NM,NM,DL,NM,NM,NM,DL,NM,DL,NM,NM,NM,DL,NM,NM},'
      '{TW,NM,NM,DL,NM,NM,NM,DW,NM,NM,NM,DL,NM,NM,TW},'
      '{NM,NM,DL,NM,NM,NM,DL,NM,DL,NM,NM,NM,DL,NM,NM},'
      '{NM,TL,NM,NM,NM,TL,NM,NM,NM,TL,NM,NM,NM,TL,NM},'
      '{NM,NM,NM,NM,DW,NM,NM,NM,NM,NM,DW,NM,NM,NM,NM},'
      '{DL,NM,NM,DW,NM,NM,NM,DL,NM,NM,NM,DW,NM,NM,DL},'
      '{NM,NM,DW,NM,NM,NM,DL,NM,DL,NM,NM,NM,DW,NM,NM},'
      '{NM,DW,NM,NM,NM,TL,NM,NM,NM,TL,NM,NM,NM,DW,NM},'
      '{TW,NM,NM,DL,NM,NM,NM,TW,NM,NM,NM,DL,NM,NM,TW}'
	'}' AS JSON), -- '{}' is json, [] is array; use json_build_array(<json type>) to extract array

	PRIMARY KEY (game_id),
	FOREIGN KEY (p1_id) REFERENCES users(user_id) ON DELETE CASCADE,
	FOREIGN KEY (p2_id) REFERENCES users(user_id) ON DELETE CASCADE,
	CHECK (p1_id <> p2_id),
	CHECK (winner >=1 AND winner <= 3)
  /*
	  0 : incomplete
	  1 : p1 wins
	  2 : p2 wins
	  3 : tie
  */
);

/*
SELECT json_build_array('{'
    '{ TW,NM,NM,DL,NM,NM,NM,TW,NM,NM,NM,DL,NM,NM,TW},'
    '{ NM,DW,NM,NM,NM,TL,NM,NM,NM,TL,NM,NM,NM,DW,NM},'
    '{ NM,NM,DW,NM,NM,NM,DL,NM,DL,NM,NM,NM,DW,NM,NM},'
    '{ DL,NM,NM,DW,NM,NM,NM,DL,NM,NM,NM,DW,NM,NM,DL},'
    '{ NM,NM,NM,NM,DW,NM,NM,NM,NM,NM,DW,NM,NM,NM,NM},'
    '{ NM,TL,NM,NM,NM,TL,NM,NM,NM,TL,NM,NM,NM,TL,NM},'
    '{ NM,NM,DL,NM,NM,NM,DL,NM,DL,NM,NM,NM,DL,NM,NM},'
    '{ TW,NM,NM,DL,NM,NM,NM,DW,NM,NM,NM,DL,NM,NM,TW},'
    '{ NM,NM,DL,NM,NM,NM,DL,NM,DL,NM,NM,NM,DL,NM,NM},'
    '{ NM,TL,NM,NM,NM,TL,NM,NM,NM,TL,NM,NM,NM,TL,NM},'
    '{ NM,NM,NM,NM,DW,NM,NM,NM,NM,NM,DW,NM,NM,NM,NM},'
    '{ DL,NM,NM,DW,NM,NM,NM,DL,NM,NM,NM,DW,NM,NM,DL},'
    '{ NM,NM,DW,NM,NM,NM,DL,NM,DL,NM,NM,NM,DW,NM,NM},'
    '{ NM,DW,NM,NM,NM,TL,NM,NM,NM,TL,NM,NM,NM,DW,NM},'
    '{ TW,NM,NM,DL,NM,NM,NM,TW,NM,NM,NM,DL,NM,NM,TW} }');
    --::text[]); -- alternative form of casting, not needed here though
*/
