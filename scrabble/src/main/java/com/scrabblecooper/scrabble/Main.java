package com.scrabble.jdbc;

import java.sql.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String... args) {
        System.out.println("Welcome To Scrabble");
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",
                "scrabble", "postgres", "password");

        try {
            Connection connection = dcm.getConnection();
/*
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT board FROM games WHERE game_id = 12");

            while(resultSet.next()){
                System.out.println(resultSet.getArray(1)); // printing an array is easier w/ sql array, like this line
            }
*/
            // Quick test of dictionary
/*
            DictionaryDAO dictionaryDAO = new DictionaryDAO (connection);

            MyDictionary myDictionary = dictionaryDAO.findDefinition ("AA");

            if (myDictionary.wasFound) myDictionary.printDictionaryInfo ();

            myDictionary = dictionaryDAO.findDefinition ("ZYZZYVA");

            if (myDictionary.wasFound) myDictionary.printDictionaryInfo ();
*/
            // Find Player

            PlayerDAO playerDAO = new PlayerDAO(connection);
            Player player = null;

            // The first player must already exist and can not be zeta at the moment as they are always the second player

            try {
                while (true) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

                    System.out.print("Username :");
                    String tempUsername = br.readLine();

                    System.out.print("Password :");
                    String tempPassword = br.readLine();

                    System.out.print(tempUsername);
                    System.out.print(" ");
                    System.out.println(tempPassword);

                    player = playerDAO.findByUsername(tempUsername);

                    if ((player.getUserName().compareToIgnoreCase(tempUsername) == 0) && (player.getPassword().compareTo(tempPassword) == 0))
                    {
                       // System.out.println(player.getPlayerId() + " " + player.getUserName() + " " + player.getPassword());
                        break;
                    }
                    else
                    {
                        player       = null;
                        tempUsername = "";
                        tempPassword = "";
                    }
                }
            }
            catch (IOException ex){
                System.out.println(ex);
            }

            System.out.println("============== BEGIN =====================");

            //Update Board
            GameDAO gameDAO = new GameDAO(connection);

            Game game = gameDAO.findByPlayerId(player.getPlayerId());

            if (game == null)
            {
                // no current games create new game
                Game tempGame = new Game ();
                tempGame.newGame(player.getPlayerId(),5); // player two ID is always 5 (Zeta) until we do match making on the web interface
                long tempGameID = gameDAO.NewGame(tempGame);
                game = gameDAO.findById(tempGameID);
            }

            Player playerTwo = null;
            playerTwo = playerDAO.findById(5);


            game.setPlayerNames (player.getUserName(), playerTwo.getUserName());

            while (true) {
                game.display ();
                game.getMove ();
                break;
            }

/*
            Array sqlBoard = game.getBoard();
            String[][] javaBoard = (String[][])sqlBoard.getArray(); //cast java sql array to java array for array properties
            javaBoard[2][2] = "JDW"; // !Note javaBoard index starts at 0, but sql array starts at 1
            System.out.println(javaBoard[3][3]);
//            System.out.println(javaBoard); // cannot print java 2D array easily
            Array updatedBoard = connection.createArrayOf("text", javaBoard); //sql array
            System.out.println(updatedBoard);
            gameDAO.updateBoard(12,updatedBoard);
//            System.out.println(game.getBoard());
*/
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

}
