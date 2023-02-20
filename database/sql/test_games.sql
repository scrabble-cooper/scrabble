INSERT INTO games (p1_id, p2_id, winner) VALUES (1,5,2);
INSERT INTO games (game_id, p1_id, p2_id, p1_score, p2_score, p1_hand, p2_hand, current_round, winner) VALUES
    (12, 2,4, 99,98,'bbccdde','eefghik',3,1);
INSERT INTO games (game_id,p1_id, p2_id, letters_left, board) VALUES (25,3,4,'aabbc',
    '{{"TW", "NM", "NM", "DL", "NM", "NM", "NM", "TW", "NM", "NM", "NM", "DL", "NM", "NM", "TW"}
    , {"NM", "DW", "NM", "NM", "NM", "TL", "NM", "NM", "NM", "TL", "NM", "NM", "NM", "DW", "NM"}
    , {"NM", "NM", "DW", "NM", "NM", "NM", "DL", "NM", "DL", "NM", "NM", "NM", "DW", "NM", "NM"}
    , {"DL", "NM", "NM", "DW", "NM", "NM", "NM", "DL", "NM", "NM", "NM", "DW", "NM", "NM", "DL"}
    , {"NM", "NM", "NM", "NM", "DW", "NM", "NM", "NM", "NM", "NM", "DW", "NM", "NM", "NM", "NM"}
    , {"NM", "TL", "NM", "NM", "NM", "TL", "NM", "NM", "NM", "TL", "NM", "NM", "NM", "TL", "NM"}
    , {"NM", "NM", "DL", "NM", "NM", "NM", "CDL", "NM", "DL", "NM", "NM", "NM", "DL", "NM", "NM"}
    , {"TW", "NM", "NM", "DL", "NM", "CNM", "ANM", "TDW", "NM", "NM", "NM", "DL", "NM", "NM", "TW"}
    , {"NM", "NM", "DL", "NM", "NM", "NM", "TDL", "NM", "DL", "NM", "NM", "NM", "DL", "NM", "NM"}
    , {"NM", "TL", "NM", "NM", "NM", "TL", "NM", "NM", "NM", "TL", "NM", "NM", "NM", "TL", "NM"}
    , {"NM", "NM", "NM", "NM", "DW", "NM", "NM", "NM", "NM", "NM", "DW", "NM", "NM", "NM", "NM"}
    , {"DL", "NM", "NM", "DW", "NM", "NM", "NM", "DL", "NM", "NM", "NM", "DW", "NM", "NM", "DL"}
    , {"NM", "NM", "DW", "NM", "NM", "NM", "DL", "NM", "DL", "NM", "NM", "NM", "DW", "NM", "NM"}
    , {"NM", "DW", "NM", "NM", "NM", "TL", "NM", "NM", "NM", "TL", "NM", "NM", "NM", "DW", "NM"}
    , {"TW", "NM", "NM", "DL", "NM", "NM", "NM", "TW", "NM", "NM", "NM", "DL", "NM", "NM", "TW"}
}
');
SELECT board FROM games WHERE game_id = 25;