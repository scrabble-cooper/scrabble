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
	board text[15][15] /*DEFAULT: "{0,0,0}" 
	{   {'TW',,,'DL',,,,'TW',,,,'DL',,,'TW',}
        {,'DW',,,,'TL',,,,'TL',,,,'DW',,}
        {,,'DW',,,,'DL',,'DL',,,,'DW',,,}
        {'DL',,,'DW',,,,'DL',,,,'DW',,,'DL',}
        {,,,,'DW',,,,,,'DW',,,,,}
        {,'TL',,,,'TL',,,,'TL',,,,'TL',,}
        {,,'DL',,,,'DL',,'DL',,,,'DL',,,}
        {'TW',,,'DL',,,,'DW',,,,'DL',,,'TW',}
        {,,'DL',,,,'DL',,'DL',,,,'DL',,,}
        {,'TL',,,,'TL',,,,'TL',,,,'TL',,}
        {,,,,'DW',,,,,,'DW',,,,,}
        {'DL',,,'DW',,,,'DL',,,,'DW',,,'DL',}
        {,,'DW',,,,'DL',,'DL',,,,'DW',,,}
        {,'DW',,,,'TL',,,,'TL',,,,'DW',,}
         {'TW',,,'DL',,,,'TW',,,,'DL',,,'TW'}
	}"*/, 
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
