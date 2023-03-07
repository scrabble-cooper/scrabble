BEGIN;

CREATE SEQUENCE game_seq start with 1; -- then next value will be 1

CREATE TABLE games (
  game_id bigint NOT NULL DEFAULT nextval('game_seq'),
	p1_id bigint NOT NULL,
	p2_id bigint NOT NULL,
    players_turn_id bigint NOT NULL, -- whos turn is it. Always the players id ie p1_id or p2_id
    passcount int NOT NULL DEFAULT 0, -- game ends in a draw if there are three passess in a row 
    start_ts timestamp DEFAULT NOW (), 
    last_update_ts timestamp DEFAULT NOW(),
	p1_score int NOT NULL DEFAULT 0,
	p2_score int NOT NULL DEFAULT 0,
	p1_hand varchar(7) NOT NULL, -- can be empty if player uses every letter in end game
	p2_hand varchar(7) NOT NULL,
	current_round int NOT NULL DEFAULT 0,
	letters_left varchar(100) NOT NULL,
	winner int DEFAULT 0,
-- 	board text[15][15] DEFAULT '{{"TL",,"T","D","T"},{"D","T","T","D","A"}}',
	board text[15][15] DEFAULT  '{{"TW", "NM", "NM", "DL", "NM", "NM", "NM", "TW", "NM", "NM", "NM", "DL", "NM", "NM", "TW"}
    , {"NM", "DW", "NM", "NM", "NM", "TL", "NM", "NM", "NM", "TL", "NM", "NM", "NM", "DW", "NM"}
    , {"NM", "NM", "DW", "NM", "NM", "NM", "DL", "NM", "DL", "NM", "NM", "NM", "DW", "NM", "NM"}
    , {"DL", "NM", "NM", "DW", "NM", "NM", "NM", "DL", "NM", "NM", "NM", "DW", "NM", "NM", "DL"}
    , {"NM", "NM", "NM", "NM", "DW", "NM", "NM", "NM", "NM", "NM", "DW", "NM", "NM", "NM", "NM"}
    , {"NM", "TL", "NM", "NM", "NM", "TL", "NM", "NM", "NM", "TL", "NM", "NM", "NM", "TL", "NM"}
    , {"NM", "NM", "DL", "NM", "NM", "NM", "DL", "NM", "DL", "NM", "NM", "NM", "DL", "NM", "NM"}
    , {"TW", "NM", "NM", "DL", "NM", "NM", "NM", "DW", "NM", "NM", "NM", "DL", "NM", "NM", "TW"}
    , {"NM", "NM", "DL", "NM", "NM", "NM", "DL", "NM", "DL", "NM", "NM", "NM", "DL", "NM", "NM"}
    , {"NM", "TL", "NM", "NM", "NM", "TL", "NM", "NM", "NM", "TL", "NM", "NM", "NM", "TL", "NM"}
    , {"NM", "NM", "NM", "NM", "DW", "NM", "NM", "NM", "NM", "NM", "DW", "NM", "NM", "NM", "NM"}
    , {"DL", "NM", "NM", "DW", "NM", "NM", "NM", "DL", "NM", "NM", "NM", "DW", "NM", "NM", "DL"}
    , {"NM", "NM", "DW", "NM", "NM", "NM", "DL", "NM", "DL", "NM", "NM", "NM", "DW", "NM", "NM"}
    , {"NM", "DW", "NM", "NM", "NM", "TL", "NM", "NM", "NM", "TL", "NM", "NM", "NM", "DW", "NM"}
    , {"TW", "NM", "NM", "DL", "NM", "NM", "NM", "TW", "NM", "NM", "NM", "DL", "NM", "NM", "TW"}
      }',

	PRIMARY KEY (game_id),
	FOREIGN KEY (p1_id) REFERENCES users(user_id) ON DELETE CASCADE,
	FOREIGN KEY (p2_id) REFERENCES users(user_id) ON DELETE CASCADE,
	-- CHECK (p1_id <> p2_id),
    /* why can not you play yourself */
	CHECK (winner >=0 AND winner <= 3)
  /*
	  0 : incomplete
	  1 : p1 wins
	  2 : p2 wins
	  3 : tie
  */
);

COMMIT;

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
    --::text[]); -- casting, not needed here though
*/
