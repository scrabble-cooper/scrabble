package com.scrabblecooper.scrabble;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

import static java.lang.System.exit;

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
			 System.out.println("Player 1: " + game.getP1Score());
			 System.out.println("Player 2: " + game.getP2Score());

			 gameDAO.updateP1Score(gameID, points); // Adds points to p1_score in games table
			 gameDAO.updateP2Score(gameID, points); // Adds points to p2_score in games table

			 //re-access data object to confirm changes
			 updatedGame = gameDAO.findById(gameID);
			 System.out.println("New score:");
			 System.out.println("Player 1: " + updatedGame.getP1Score());
			 System.out.println("Player 2: " + updatedGame.getP2Score());
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
			System.out.println("Old hand:");
			System.out.println("Player 1: " + game.getP1Hand());
			System.out.println("Player 2: " + game.getP2Hand());

			gameDAO.drawFromP1Hand(gameID, letter); // Removes letter from p1_hand in games table
			gameDAO.drawFromP2Hand(gameID, letter); // Removes letter from p2_hand in games table

			//re-access data object to confirm changes
			updatedGame = gameDAO.findById(gameID);
			System.out.println("New hand:");
			System.out.println("Player 1: " + updatedGame.getP1Hand());
			System.out.println("Player 2: " + updatedGame.getP2Hand());
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/drawIntoHand")
	public void drawIntoHand(@RequestBody handMSG hdMSG){
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
			System.out.println("Old hand:");
			System.out.println("Player 1: " + game.getP1Hand());
			System.out.println("Player 2: " + game.getP2Hand());

			gameDAO.drawIntoP1Hand(gameID, letter); // Adds letter into p1_hand in games table
			gameDAO.drawIntoP2Hand(gameID, letter); // Adds letter into p2_hand in games table

			//re-access data object to confirm changes
			updatedGame = gameDAO.findById(gameID);
			System.out.println("New hand:");
			System.out.println("Player 1: " + updatedGame.getP1Hand());
			System.out.println("Player 2: " + updatedGame.getP2Hand());
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println("Hello Spring Boot");
		ApplicationContext applicationContext = SpringApplication.run(ScrabbleApplication.class);
		Controller service = applicationContext.getBean(Controller.class);
		service.Controller();
		// SpringApplication.run(ScrabbleApplication.class, args); //keep this line only
	}
}

@Service
class Controller{
	public void Controller(){
		try{
			while (true) {
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("[1] Create Account");
				System.out.println("[2] Log In");
				System.out.println("[3] Exit");

				int startChoice = Integer.parseInt(br.readLine());

				if(startChoice == 1){
					createAccountController();
				}
				if(startChoice == 2){
//					loginController();
				}
				if(startChoice == 3){
					exit(0);
				}
			}
		}
		catch(IOException ex){
			System.out.println(ex);
		}
	}

	public void createAccountController(){
		DatabaseConnectionManager dcm = new DatabaseConnectionManager("db",
				"scrabble", "postgres", "password");

		// Player tempPlayer = new Player();

		try{
			while(true) {
				Connection connection = dcm.getConnection();
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

				System.out.print("Username :");
				String username = br.readLine();
				System.out.print("Password :");
				String password = br.readLine();

				Player player = new Player();
				player.setUserName(username);
				player.setPassword(password);
				PlayerDAO playerDAO = new PlayerDAO(connection);
				playerDAO.create(player);

				System.out.println();
				System.out.println("Account created!");
				System.out.println("Username: " + player.getUserName());
				System.out.println("Password: " + player.getPassword());

				// tempPlayer = playerDAO.findByUsername(tempUsername);

				// if ((player.getUserName().compareToIgnoreCase(tempUsername) == 0) && (player.getPassword().compareTo(tempPassword) == 0))
				// {
				// 	// System.out.println(player.getPlayerId() + " " + player.getUserName() + " " + player.getPassword());
				// 	break;
				// }
				// else
				// {
				// 	player       = null;
				// 	tempUsername = "";
				// 	tempPassword = "";
				// }

				newGameController(player);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		catch(IOException ex){
			System.out.println(ex);
		}
	}

	public void newGameController(Player p1){
		DatabaseConnectionManager dcm = new DatabaseConnectionManager("db",
				"scrabble", "postgres", "password");

		try{
			while(true) {
				Connection connection = dcm.getConnection();
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

				Player p2 = new Player();
				p2.setUserName("computer");
				p2.setPassword("123");
				PlayerDAO playerDAO = new PlayerDAO(connection);
				playerDAO.create(p2);

				System.out.println("============== BEGIN =====================");

				GameDAO gameDAO = new GameDAO(connection);

				System.out.println("Done until here");


				// tempPlayer = playerDAO.findByUsername(tempUsername);

				// if ((player.getUserName().compareToIgnoreCase(tempUsername) == 0) && (player.getPassword().compareTo(tempPassword) == 0))
				// {
				// 	// System.out.println(player.getPlayerId() + " " + player.getUserName() + " " + player.getPassword());
				// 	break;
				// }
				// else
				// {
				// 	player       = null;
				// 	tempUsername = "";
				// 	tempPassword = "";
				// }

			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}

}
