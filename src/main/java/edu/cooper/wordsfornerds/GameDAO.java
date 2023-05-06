package edu.cooper.wordsfornerds;
import edu.cooper.wordsfornerds.DataAccessObject;

import static edu.cooper.wordsfornerds.MainApp.dbconnection;

import org.apache.catalina.core.AprLifecycleListener;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Random;




import java.sql.*;

public class GameDAO extends DataAccessObject {

    private static final String ALL_LETTERS =  "AAAAAAAAABBCCDDDDEEEEEEEEEEEEFFGGGHHIIIIIIIIIJKLLLLMMNNNNNNOOOOOOOOPPQRRRRRRSSSSTTTTTTUUUUVVWWXYYZ**";
    private static final String GET_GAME= "SELECT * FROM games WHERE game_id=?";
//    private static final String UPDATE_GAME = "UPDATE games SET board = ?::TEXT[][] WHERE game_id=?"; //no cast needed
    private static final String UPDATE_GAME = "UPDATE games SET board = ? WHERE game_id=?";
    private static final String GET_GAME_BY_PLAYER_ID = "SELECT DISTINCT * FROM games WHERE (p1_id=? OR p2_id=?) ORDER BY game_id DESC";
    private static final String UPDATE_WHOLE_GAME = "UPDATE games SET players_turn_id=?, passcount=?, last_update_ts = NOW(), current_round=?, " +
            "p1_score=? , p2_score=?, p1_hand=?, p2_hand=?, letters_left=?, winner=? , board = ?::TEXT[][] WHERE game_id=? ";
    // Update almost the whole table

    private static final String CREATE_GAME = "INSERT INTO games (p1_id, p1_username) VALUES (?,?) RETURNING game_id";

    private static final String JOINABLE_GAMES = "SELECT game_id, user_name AS opponent_name FROM games, users WHERE " +
            " p2_id IS NULL AND p1_id != ? AND p1_id = users.user_id ORDER BY game_id DESC";

    private static final String JOIN_GAME_ME_FIRST = "UPDATE games SET p2_id = ?, p2_username = ?,  players_turn_id = p1_id, start_ts = NOW (), last_update_ts = NOW(), " +
                        " p1_hand = ?, p2_hand = ?, letters_left = ?, current_round = 1 WHERE game_id = ? AND p2_id IS NULL RETURNING p1_id";
    private static final String JOIN_GAME_ME_SECOND = "UPDATE games SET p2_id = ?, p2_username = ?, start_ts = NOW (), last_update_ts = NOW(), " +
            " p1_hand = ?, p2_hand = ?, letters_left = ?, players_turn_id = ?, current_round = 1  WHERE game_id = ? AND p2_id IS NULL RETURNING p1_id";

    private static final String MOVE_PLAY = "UPDATE games SET last_update_ts = NOW(), challengecount = ?, passcount = 0, players_turn_id = ?, lastplay = ?, " +
                                            " p1_hand = ?, p2_hand = ?, playlog = ? WHERE game_id = ?";

    private static final String MOVE_PASS = "UPDATE games SET last_update_ts = NOW(), challengecount = 0, passcount = ?, players_turn_id = ?, " +
            "  current_round = ?, winner = ?, playlog = ? WHERE game_id = ?";
    private static final String MOVE_EXCHANGE = "UPDATE games SET last_update_ts = NOW(), challengecount = 0, passcount = 0, players_turn_id = ?, " +
            "  p1_hand = ?, p2_hand = ?, letters_left = ?, current_round = ?, playlog = ? WHERE game_id = ?";

    private static final String MAKE_PERMANENT = "UPDATE games SET last_update_ts = NOW(), passcount = ?, p1_score = ?, p2_score = ?, " +
            " p1_hand = ?, p2_hand = ?, current_round = ? , letters_left = ? , " +
            " challengecount = ?, board = ?, lastplay = ?, playedby = ?, winner = ?, players_turn_id = ?, playlog = ?  WHERE game_id = ?";

    private static final String UNMAKE_LAST_MOVE = "UPDATE games SET last_update_ts = NOW(),  p1_hand = ?, p2_hand = ?, current_round = ? ," +
            " players_turn_id = ?, challengecount = ?, lastplay = ?, playlog = ?      WHERE game_id = ?";

    public GameDAO() {
        super(dbconnection);
    }

    private String tempBag;
    private String dealLetters (int dealNLetters, String initialBag)
    {
        String Result = "";
        String letterBag = initialBag;
        Random rand = new Random();

        int  numberOfLettersLeft = letterBag.length();
        while ((numberOfLettersLeft > 1) && (dealNLetters > 0))
        {
            int i = rand.nextInt(numberOfLettersLeft);

            //System.out.println(letterBag.substring(i,i+1));

            String dealtLetter = letterBag.substring(i,i+1);

            if (i == 0)
            {
                letterBag = letterBag.substring (i+1);
            }
            else letterBag = letterBag.substring (0,i) + letterBag.substring (i+1);

            Result += dealtLetter;

            dealNLetters--;
            numberOfLettersLeft = letterBag.length();
        }
        tempBag = letterBag;
        return Result;
    }

    public long joinGame (Long game_id, Long joining_user_id, String joiningUserName){
        Random rand = new Random();
        long Result = 0;

        if (rand.nextInt (1) == 1) {
            try (PreparedStatement statement = this.connection.prepareStatement(JOIN_GAME_ME_FIRST);) {
                statement.setLong(1, joining_user_id);
                statement.setString(2, joiningUserName);


                String Bag = ALL_LETTERS;
                String pOneRack = dealLetters(7, Bag);
                Bag = tempBag;
                String pTwoRack = dealLetters(7, Bag);
                Bag = tempBag;
                statement.setString(3, pOneRack);
                statement.setString(4, pTwoRack);
                statement.setString(5, Bag);
                statement.setLong  (6, game_id);

                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    Result = rs.getLong("p1_id");
                }
                else {
                    // Probably someone has already joined this game first
                    Result = 0;
                }



            } catch (SQLException e) {
                e.printStackTrace();
                Result = 0;
                throw new RuntimeException(e);
            }
        }
        else
        {
            try (PreparedStatement statement = this.connection.prepareStatement(JOIN_GAME_ME_SECOND);) {
                statement.setLong(1, joining_user_id);

                statement.setString(2, joiningUserName);
                String Bag = ALL_LETTERS;
                String pOneRack = dealLetters(7, Bag);
                Bag = tempBag;
                String pTwoRack = dealLetters(7, Bag);
                Bag = tempBag;
                statement.setString(3, pOneRack);
                statement.setString(4, pTwoRack);
                statement.setString(5, Bag);
                statement.setLong  (6, joining_user_id);
                statement.setLong  (7, game_id);

                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    Result = rs.getLong("p1_id");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                Result = 0;
                throw new RuntimeException(e);
            }
        }
        return Result;
    }

    public JSONArray joinableGames (Long userId){
       JSONArray obj = new JSONArray();
       try(PreparedStatement statement = this.connection.prepareStatement(JOINABLE_GAMES);) {
           statement.setLong(1, userId);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                // there is at least one row in the result set pack the columns into a json object
                // and put that object into a json array
                //rs.beforeFirst(); can not use as not scrollable result
                int i = 0;
                do {
                    JSONObject tempJobj = new JSONObject();
                    tempJobj.put ("game_id", rs.getLong("game_id"));
                    tempJobj.put ("opponent_name", rs.getString("opponent_name"));
                    obj.put      (i,tempJobj);
                    i++;
                }
                while(rs.next());
            }
        } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
        }
        return obj;
    }


    public long NewGame (Long userId, String username){
        long newGameID = -1;

        try(PreparedStatement statement = this.connection.prepareStatement(CREATE_GAME);) {
            statement.setLong(1, userId);
            statement.setString(2, username);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                newGameID = rs.getLong("game_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return newGameID;
    }

    public void updateWholeGame (Game game){
        String updatedBoard = game.getBoard();

        try(PreparedStatement statement = this.connection.prepareStatement(UPDATE_WHOLE_GAME);) {

            statement.setLong   (1, game.getPlayersTurnID());
            statement.setInt    (2, game.getPassCount());
            statement.setInt    (3, game.getCurrentRound());

            statement.setInt    (4, game.getP1Score());
            statement.setInt    (5, game.getP2Score());

            statement.setString (6, game.getP1Hand());
            statement.setString (7, game.getP2Hand());

            statement.setString (8, game.getLetterBag());

            statement.setInt    (9, game.getWinner());

            statement.setString (10, updatedBoard);

            statement.setLong    (11, game.getGameId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public JSONArray findByPlayerId(long playerID){
        JSONArray obj = new JSONArray();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_GAME_BY_PLAYER_ID);) {
            statement.setLong(1, playerID);
            statement.setLong(2, playerID);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                // there is at least one row in the result set pack the columns into a json object
                // and put that object into a json array
                //rs.beforeFirst(); can not use as not scrollable result
                int i = 0;
                do {
                    JSONObject tempJobj = new JSONObject();
                    tempJobj.put ("game_id", rs.getLong("game_id"));
                    tempJobj.put ("p1_iD",rs.getLong("p1_iD"));
                    tempJobj.put ("p2_iD",rs.getLong("p2_iD"));
                    tempJobj.put ("p1_score",rs.getInt("p1_score"));
                    tempJobj.put ("p2_score",rs.getInt("p2_score"));

                    tempJobj.put ("p1_username",rs.getString("p1_username"));
                    tempJobj.put ("p2_username",rs.getString("p2_username"));

                    tempJobj.put ("letters_left",rs.getString("letters_left"));
                    tempJobj.put ("current_round",rs.getInt("current_round"));
                    tempJobj.put ("p1_hand",rs.getString("p1_hand"));
                    tempJobj.put ("p2_hand",rs.getString("p2_hand"));
                    tempJobj.put ("winner",rs.getInt("winner"));
                    tempJobj.put ("board",rs.getString("board"));
                    tempJobj.put ("lastplay",rs.getString("lastplay"));
                    tempJobj.put ("playedby",rs.getString("playedby"));
                    tempJobj.put ("challengecount",rs.getLong("challengecount"));
                    tempJobj.put ("players_turn_id",rs.getLong("players_turn_id"));
                    tempJobj.put ("playlog",rs.getString("playlog"));

                    tempJobj.put ("passcount",rs.getInt("passcount"));
                    obj.put      (i,tempJobj);
                    i++;
                }
                while(rs.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return obj;
    }

    public JSONObject findById(long id){
        JSONObject tempJobj = new JSONObject();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_GAME);) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                tempJobj.put ("game_id", rs.getLong("game_id"));
                tempJobj.put ("p1_iD",rs.getLong("p1_iD"));
                tempJobj.put ("p2_iD",rs.getLong("p2_iD"));
                tempJobj.put ("p1_username",rs.getString("p1_username"));
                tempJobj.put ("p2_username",rs.getString("p2_username"));

                tempJobj.put ("p1_score",rs.getInt("p1_score"));
                tempJobj.put ("p2_score",rs.getInt("p2_score"));
                tempJobj.put ("letters_left",rs.getString("letters_left"));
                tempJobj.put ("current_round",rs.getInt("current_round"));
                tempJobj.put ("p1_hand",rs.getString("p1_hand"));
                tempJobj.put ("p2_hand",rs.getString("p2_hand"));
                tempJobj.put ("winner",rs.getInt("winner"));
                tempJobj.put ("board",rs.getString("board"));
                tempJobj.put ("players_turn_id",rs.getLong("players_turn_id"));
                tempJobj.put ("lastplay",rs.getString("lastplay"));
                tempJobj.put ("playedby",rs.getString("playedby"));
                tempJobj.put ("challengecount",rs.getLong("challengecount"));
                tempJobj.put ("passcount",rs.getInt("passcount"));
                tempJobj.put ("playlog",rs.getString("playlog"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return tempJobj;
    }

    public void updateBoard(long gameId, Array updatedBoard) {
        try(PreparedStatement statement = this.connection.prepareStatement(UPDATE_GAME);) {
            statement.setArray(1,updatedBoard);
            statement.setLong(2, gameId);
//            System.out.println(statement); // print statement will show =? for board param, but works regardless
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void movePlay (String message) {
        JSONObject jo = new JSONObject(message);

        try (PreparedStatement statement = this.connection.prepareStatement(MOVE_PLAY);) {

            statement.setInt    (1,  jo.getInt    ("challengecount"  ));
            statement.setLong   (2,  jo.getLong   ("players_turn_id" ));
            statement.setString (3,  jo.getString ("lastplay"        ));
            statement.setString (4,  jo.getString ("p1_hand"         ));
            statement.setString (5,  jo.getString ("p2_hand"         ));
            statement.setString (6,  jo.getString ("playlog"         ));
            statement.setLong   (7,  jo.getLong   ("game_id"         ));

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void lastMoveMakePermanent (String message) {

        JSONObject jo = new JSONObject(message);


        try (PreparedStatement statement = this.connection.prepareStatement(MAKE_PERMANENT);) {

            statement.setInt     (1,  jo.getInt    ("passcount"));
            statement.setInt     (2,  jo.getInt    ("p1_score" ));
            statement.setInt     (3,  jo.getInt    ("p2_score" ));
            statement.setString  (4,  jo.getString ("p1_hand"  ));
            statement.setString  (5,  jo.getString ("p2_hand"  ));
            statement.setInt     (6,  jo.getInt    ("current_round"  ));
            statement.setString  (7,  jo.getString ("letters_left"   ));
            statement.setInt     (8,  jo.getInt    ("challengecount" ));
            statement.setString  (9,  jo.getString ("board"          ));
            statement.setString  (10, jo.getString ("lastplay"       ));
            statement.setString  (11, jo.getString ("playedby"       ));
            statement.setInt     (12, jo.getInt    ("winner"         ));
            statement.setLong    (13, jo.getLong   ("players_turn_id"));
            statement.setString  (14, jo.getString ("playlog"         ));
            statement.setLong    (15, jo.getLong   ("game_id"        ));


            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void unMakeLastMove (String message) {

        JSONObject jo = new JSONObject(message);

        try (PreparedStatement statement = this.connection.prepareStatement(UNMAKE_LAST_MOVE);) {

            statement.setString  (1,  jo.getString ("p1_hand"  ));
            statement.setString  (2,  jo.getString ("p2_hand"  ));
            statement.setInt     (3,  jo.getInt    ("current_round"  ));
            statement.setLong    (4,  jo.getLong  ("players_turn_id" ));

            statement.setInt     (5,  jo.getInt    ("challengecount" ));
            statement.setString  (6,  jo.getString ("lastplay"       ));
            statement.setString  (7,  jo.getString ("playlog"         ));
            statement.setLong    (8,  jo.getLong   ("game_id"        ));

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }



    public void movePass (String message) {
        JSONObject jo = new JSONObject(message);

        try (PreparedStatement statement = this.connection.prepareStatement(MOVE_PASS);) {

            Long game_id         = jo.getLong("game_id");
            Long players_turn_id = jo.getLong("players_turn_id");
            int  passcount       = jo.getInt ("passcount");
            int current_round    = jo.getInt("current_round");

            statement.setInt    (1, passcount);
            statement.setLong   (2, players_turn_id);
            statement.setInt    (3, current_round);
            statement.setInt    (4, jo.getInt("winner"));
            statement.setString (5,  jo.getString ("playlog"));
            statement.setLong   (6, game_id);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void moveExchange (String message) {
        JSONObject jo = new JSONObject(message);

        try (PreparedStatement statement = this.connection.prepareStatement(MOVE_EXCHANGE);) {

            Long game_id         = jo.getLong("game_id");
            Long players_turn_id = jo.getLong("players_turn_id");

            int current_round = jo.getInt("current_round");

            String p1_hand       = jo.getString("p1_hand");
            String p2_hand       = jo.getString("p2_hand");
            String letters_left  = jo.getString("letters_left");

            statement.setLong   (1, players_turn_id);
            statement.setString (2, p1_hand);
            statement.setString (3, p2_hand);
            statement.setString (4, letters_left);
            statement.setInt    (5, current_round);
            statement.setString (6, jo.getString ("playlog"));
            statement.setLong   (7, game_id);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}

