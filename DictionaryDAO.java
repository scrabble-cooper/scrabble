package com.scrabble.jdbc;

import com.scrabble.jdbc.util.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE;

public  class DictionaryDAO extends DataAccessObject {
    private static final String GET_DEFINITION = "SELECT definition " +
            "FROM dictionary.words WHERE word=?";

    public DictionaryDAO(Connection connection) {
        super(connection);
    }

    public MyDictionary findById(long id){
        return null;
    }
    public MyDictionary findDefinition (String word_text) {
        MyDictionary mydictionary = new MyDictionary ();
        mydictionary.theWord = word_text;

        try(PreparedStatement statement = this.connection.prepareStatement(GET_DEFINITION,TYPE_SCROLL_INSENSITIVE);) {
            statement.setString(1, word_text);
            ResultSet rs = statement.executeQuery();

            if (rs.next ()) {
                if (rs.getRow() == 1) {
                    mydictionary.wasFound = true;
                    mydictionary.wordDefinition = (rs.getString("definition"));
                }
                else {
                    System.out.println("Multiple definitions found.");
                }
            }
            else {
                // user_name not found
                System.out.println("Username not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return mydictionary;
    }
}
