package com.scrabblecooper.scrabble;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.*;

@SpringBootApplication
@RestController
public class ScrabbleApplication {

	// Write edge case checks for robustness
	// Shift to a controller annotation (put in separate package)
	@PostMapping("/updateBoard")
	public void updateBoard (@RequestBody boardMSG bdMSG){
		DatabaseConnectionManager dcm = new DatabaseConnectionManager("db",
				"scrabble", "postgres", "password");
		Game game = new Game();
		Game updatedGame = new Game();
		Array retrieveUpdatedBoard;
		try {
			Connection connection = dcm.getConnection();
			//Update Board
			GameDAO gameDAO = new GameDAO(connection);
			int gameID = bdMSG.getGameID();
			int row = bdMSG.getBoardRow();
			int col = bdMSG.getBoardCol();
			game = gameDAO.findById(gameID);
			Array sqlBoard = game.getBoard();
			// print Old
			System.out.println("Old Board");
			System.out.println(sqlBoard);

			// Update
			String[][] javaBoard = (String[][])sqlBoard.getArray(); //cast java sql array to java array for array properties
			String charUpdate = bdMSG.getCharToChange();
			javaBoard[row][col] = charUpdate;// !Note javaBoard index starts at 0, but sql array starts at 1
//            System.out.println(javaBoard); // cannot print java 2D array easily
			Array updatedBoard = connection.createArrayOf("text", javaBoard); //sql array
			gameDAO.updateBoard(gameID,updatedBoard);
			updatedGame = gameDAO.findById(gameID);
			System.out.println(row +" " + col +" " + charUpdate);

			//print New
			System.out.println("New Board");
			System.out.println(updatedGame.getBoard());
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
//		retrieveUpdatedBoard = updatedGame.getBoard(); // game should be filled by now, may need fix
//		return retrieveUpdatedBoard;
//		return;
	}

	 @PostMapping("/updatePoints")
	 public void updatePoints(@RequestBody pointMSG ptMSG){
		 DatabaseConnectionManager dcm = new DatabaseConnectionManager("db",
				 "scrabble", "postgres", "password");
		 Game game = new Game();
		 Game updatedGame = new Game();

		 try {
			 Connection connection = dcm.getConnection();
			 //Update Game
			 GameDAO gameDAO = new GameDAO(connection);
			 int gameID = ptMSG.getGameID();
			 int points = ptMSG.getPoints();

			 game = gameDAO.findById(gameID);
			 System.out.println("Old score:");
			 System.out.println(game.getP1Score());
			 System.out.println(game.getP2Score());

			 gameDAO.updateP1Score(gameID, points); // Adds points to p1_score in games table
			 gameDAO.updateP2Score(gameID, points); // Adds points to p2_score in games table

			 //re-access data object to confirm changes
			 updatedGame = gameDAO.findById(gameID);
			 System.out.println("New score:");
			 System.out.println(updatedGame.getP1Score());
			 System.out.println(updatedGame.getP2Score());
		 }
		 catch(SQLException e) {
			 e.printStackTrace();
		 }
	 }

	@PostMapping("/drawFromHand")
	public void drawFromHand(@RequestBody handMSG hdMSG){
		DatabaseConnectionManager dcm = new DatabaseConnectionManager("db",
				"scrabble", "postgres", "password");
		Game game = new Game();
		Game updatedGame = new Game();

		try {
			Connection connection = dcm.getConnection();
			//Update Game
			GameDAO gameDAO = new GameDAO(connection);
			int gameID = hdMSG.getGameID();
			String letter = hdMSG.getLetter();

			game = gameDAO.findById(gameID);
			System.out.println("Old score:");
			System.out.println(game.getP1Hand());
			System.out.println(game.getP2Hand());

			gameDAO.drawFromP1Hand(gameID, letter); // Removes letter from p1_hand in games table
			gameDAO.drawFromP2Hand(gameID, letter); // Removes letter from p2_hand in games table

			//re-access data object to confirm changes
			updatedGame = gameDAO.findById(gameID);
			System.out.println("New score:");
			System.out.println(updatedGame.getP1Hand());
			System.out.println(updatedGame.getP2Hand());
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		System.out.println("Hello Spring Boot");
		SpringApplication.run(ScrabbleApplication.class, args); //keep this line only

	}
}

