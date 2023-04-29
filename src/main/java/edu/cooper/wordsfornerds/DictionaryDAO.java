package edu.cooper.wordsfornerds;

import org.json.JSONArray;
import org.json.JSONObject;

import static edu.cooper.wordsfornerds.MainApp.dbconnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE;

public  class DictionaryDAO extends DataAccessObject {
    private static final String GET_DEFINITION = "SELECT definition FROM dictionary.words WHERE word=?";
    private static final String GET_MULTI_DEF_PREFIX = "SELECT A.word, coalesce(B.definition,'Oops!. Not in Dictionary.') as definition FROM (( VALUES ";

    private static final String GET_MULTI_DEF_SUFFIX = ") AS A (word) LEFT JOIN words B on A.word = B.word) ORDER BY A.word ASC;";

    public DictionaryDAO() {
        super(dbconnection);
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

    public JSONArray checkWords(String wordList) {
        // wordList multiple words must be separated by __  ie from javascript row ['word'] + " __ "; //
        JSONArray obj = new JSONArray();

        String theWordList = " ('" + wordList.toUpperCase().replaceAll(" __ ", "'), ('") + "')";

        String sqlQuery = GET_MULTI_DEF_PREFIX;
        sqlQuery += theWordList;
        sqlQuery += GET_MULTI_DEF_SUFFIX;

        try(PreparedStatement statement = this.connection.prepareStatement(sqlQuery);) {
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                // there is at least one row in the result set pack the columns into a json object
                // and put that object into a json array
                //rs.beforeFirst(); can not use as not scrollable result
                int i = 0;
                do {
                    JSONObject tempJobj = new JSONObject();
                    tempJobj.put ("word", rs.getString("word"));
                    tempJobj.put ("definition",rs.getString("definition"));
                    obj.put      (i,tempJobj);
                    i++;
                }
                while(rs.next());
            }       } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return obj;
    }


}
