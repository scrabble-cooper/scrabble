package com.scrabblecooper.scrabble;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.*;
import java.util.List;


@SpringBootApplication
@RestController
public class ScrabbleApplication {

	// Write edge case checks for robustness
	@PostMapping("/updateBoard")
	public void updateBoard (@RequestBody String msg){
		DatabaseConnectionManager dcm = new DatabaseConnectionManager("db",
				"scrabble", "postgres", "password");
		Game game = new Game();
		Game updatedGame = new Game();
		Array retrieveUpdatedBoard;
		try {
			Connection connection = dcm.getConnection();
			//Update Board
			GameDAO gameDAO = new GameDAO(connection);
			int gameID = Integer.parseInt(msg);
			game = gameDAO.findById(gameID);
			Array sqlBoard = game.getBoard();
			System.out.println(game.getP1Score());
			System.out.println(game.getP2Score());
			String[][] javaBoard = (String[][])sqlBoard.getArray(); //cast java sql array to java array for array properties
			javaBoard[2][2] = "YDW"; // !Note javaBoard index starts at 0, but sql array starts at 1
			System.out.println(javaBoard[3][3]);
			System.out.println("Hello");
//            System.out.println(javaBoard); // cannot print java 2D array easily
			Array updatedBoard = connection.createArrayOf("text", javaBoard); //sql array
			System.out.println(updatedBoard);
			gameDAO.updateBoard(gameID,updatedBoard);
			System.out.println("Hello");



			int points = 5;
			gameDAO.updateP1Score(gameID, points); // Adds on 5 points to p1_score in games table
			gameDAO.updateP2Score(gameID, points); // Adds on 5 points to p2_score in games table

			//re-access data object to confirm changes
			updatedGame = gameDAO.findById(gameID);
			System.out.println(updatedGame.getBoard());
			System.out.println(updatedGame.getP1Score());
			System.out.println(updatedGame.getP2Score());


		}
		catch(SQLException e) {
			e.printStackTrace();
		}
//		retrieveUpdatedBoard = updatedGame.getBoard(); // game should be filled by now, may need fix
//		return retrieveUpdatedBoard;
//		return;
	}

	 @PostMapping("/updatePoints")
	 public void updatePoints(@RequestBody String msg){
		 DatabaseConnectionManager dcm = new DatabaseConnectionManager("db",
				 "scrabble", "postgres", "password");
		 Game game = new Game();

		 try {
			 Connection connection = dcm.getConnection();
			 //Update Board
			 GameDAO gameDAO = new GameDAO(connection);
			 int gameID = Integer.parseInt(msg);
			 game = gameDAO.findById(gameID);
			 Array sqlBoard = game.getBoard();
			 System.out.println(game.getP1Score());
			 System.out.println(game.getP2Score());
			 String[][] javaBoard = (String[][])sqlBoard.getArray(); //cast java sql array to java array for array properties
			 javaBoard[2][2] = "YDW"; // !Note javaBoard index starts at 0, but sql array starts at 1
			 System.out.println(javaBoard[3][3]);
			 System.out.println("Hello");
//            System.out.println(javaBoard); // cannot print java 2D array easily
			 Array updatedBoard = connection.createArrayOf("text", javaBoard); //sql array
			 System.out.println(updatedBoard);
			 gameDAO.updateBoard(gameID,updatedBoard);
			 System.out.println("Hello");



			 int points = 5;
			 gameDAO.updateP1Score(gameID, points); // Adds on 5 points to p1_score in games table
			 gameDAO.updateP2Score(gameID, points); // Adds on 5 points to p2_score in games table

			 //re-access data object to confirm changes
			 updatedGame = gameDAO.findById(gameID);
			 System.out.println(updatedGame.getBoard());
			 System.out.println(updatedGame.getP1Score());
			 System.out.println(updatedGame.getP2Score());


		 }
		 catch(SQLException e) {
			 e.printStackTrace();
		 }
	 }


	public static void main(String[] args) { // move all but one line out of main after demo for better practice
		System.out.println("Hello Spring Boot");
		SpringApplication.run(ScrabbleApplication.class, args); //keep this line only

		/*
		DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",
				"scrabble", "postgres", "password");

		try {
			Connection connection = dcm.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT board FROM games WHERE game_id = 12");
			//Print rs

			while(resultSet.next()){
				System.out.println(resultSet.getArray(1)); // printing an array is easier w/ sql array, like this line
			}


		}
		catch(SQLException e) {
			e.printStackTrace();
		}

		 */
	}
}

