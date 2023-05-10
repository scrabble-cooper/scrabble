package edu.cooper.wordsfornerds;
import edu.cooper.wordsfornerds.DataTransferObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

public class Game implements DataTransferObject {
    {
        // Game class Initialization
        rnd = new Random ();
        rnd.setSeed(12345678); //  when game testing so moves repeat and errors can be reproduced fixed
        // rnd.setSeed(some time function); // when game development is finished .
        letterBag = ALL_LETTERS;
        p1Hand    = "";
        p2Hand    = "";
        p1Name    = "";
        p2Name    = "";

    }

    private final static String ALL_LETTERS =  "AAAAAAAAABBCCDDDDEEEEEEEEEEEEFFGGGHHIIIIIIIIIJKLLLLMMNNNNNNOOOOOOOOPPQRRRRRRSSSSTTTTTTUUUUVVWWXYYZ**"; // Do not forget the blanks
    private final static long boardWidth = 15;
    private final static long boardDepth = 15;

                                         // a  b  c  d  e  f  g  h  i  j  k  l  m  n  o  p  q   r  s  t  u  v  w  x  y  z
    private static int [] letterValues = {  1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10 };


    private Connection connection;

    public void setConnection (Connection conn) // something is wrong in the layering we need to sort it out
    {
       connection = conn;
    }

    private static Random rnd;
    private long gameId;
    private long p1ID;
    private long p2ID;
    private long PlayersTurnID; // ID of player whos turn it is
    private int p1Score;
    private int p2Score;
    private String letterBag;
    private int currentRound;

    private int passCount;
    private String p1Hand;
    private String p2Hand;
    private int winner;
    private String board; // always 225 characters long

    private String p1Name ; // Get from player record
    private String p2Name ;

    public long getGameId() {
        return gameId;
    }

    public void setPassCount(int pCount) {  passCount = pCount; }
    public int getPassCount() {  return passCount ; }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public long getP1ID() {
        return p1ID;
    }

    public void setP1ID(long p1ID) {
        this.p1ID = p1ID;
    }

    public long getP2ID() {
        return p2ID;
    }

    public void setP2ID(long p2ID) {
        this.p2ID = p2ID;
    }
    public void setPlayersTurnID(long playersTurnID) {
        PlayersTurnID = playersTurnID;
    }
    public long getPlayersTurnID() { return PlayersTurnID ; }

    public int getP1Score() {
        return p1Score;
    }

    public void setP1Score(int p1Score) {
        this.p1Score = p1Score;
    }

    public int getP2Score() {
        return p2Score;
    }

    public void setP2Score(int p2Score) {
        this.p2Score = p2Score;
    }

    public String getLetterBag() {
        return letterBag;
    }

    public void setLetterBag(String letterBag) {
        this.letterBag = letterBag;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public String getP1Hand() {
        return p1Hand;
    }

    public void setP1Hand(String p1Hand) {
        this.p1Hand = p1Hand;
    }

    public String getP2Hand() {
        return p2Hand;
    }

    public void setP2Hand(String p2Hand) {
        this.p2Hand = p2Hand;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public String getBoard() {
        return board;
    }



    public void setBoard(String theBoard) {

        board = theBoard;

    }

    public long getId() {
        return gameId;
    }


    public int letterValue (char letter)
    {
        int result = 0;
        int index = ((int) letter ) - 65;

        if ((index >= 0) && (index <= 25)) result = letterValues [index];

        return result;
    }
}