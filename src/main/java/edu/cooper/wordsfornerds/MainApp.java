package edu.cooper.wordsfornerds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication
public class MainApp {

	// Maybe there is somewhere to add the values for this parameter XXXXX find out later. also can not use localhost must use 127.0.0.1
	// local host may resolve as ::1 ip6 or may not resolve at all
	public static DatabaseConnectionManager dcm = new DatabaseConnectionManager("db",
			"scrabble", "postgres", "password");
    public static Connection dbconnection;

 	public static void main(String[] args) {

		try {
			dbconnection = dcm.getConnection();
		}
		catch(SQLException e) { e.printStackTrace(); }

		SpringApplication.run(MainApp.class, args);
	}
}
