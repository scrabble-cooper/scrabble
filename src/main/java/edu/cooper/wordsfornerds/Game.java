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

    private final static String ALL_LETTERS =  "AAAAAAAAABBCCDDDDEEEEEEEEEEEEFFGGGHHIIIIIIIIIJKLLLLMMNNNNNNOOOOOOOOPPQRRRRRRSSSSTTTTTTUUUUVVWWXYYZ  "; // Do not forget the blanks
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


    //    public void setBoard(String board) {
    public void setBoard(String theBoard) {

        board = theBoard;

    }

    public long getId() {
        return gameId;
    }

    private int dealLetters (int dealNLetters, int playerOneOrTwo) // 1 or 2
    {
        int  numberOfLettersLeft = letterBag.length();
        while ((numberOfLettersLeft > 1) && (dealNLetters > 0))
        {
            int i = rnd.nextInt(numberOfLettersLeft);

            //System.out.println(letterBag.substring(i,i+1));

            String dealLetter = letterBag.substring(i,i+1);

            if (i == 0)
            {
                letterBag = letterBag.substring (i+1);
            }
            else letterBag = letterBag.substring (0,i) + letterBag.substring (i+1);

            if (playerOneOrTwo == 1 ) {
                p1Hand = p1Hand + dealLetter;
            }
            else p2Hand = p2Hand + dealLetter;

            dealNLetters--;
            numberOfLettersLeft = letterBag.length();
        }
        return numberOfLettersLeft;
    }
    public void newGame (long PlayerOneID, long PlayerTwoID)
    {
        p1ID = PlayerOneID;
        p2ID = PlayerTwoID;

        if (rnd.nextInt(1) == 1) {
            PlayersTurnID = PlayerOneID;
        }
        else PlayersTurnID = PlayerTwoID;

        dealLetters (7, 1);
        dealLetters (7, 2);
    }

    public void displayBoard () {
        System.out.print("   ");

        for (int i = 0; i < 225; i++) {
            System.out.print(board.charAt(i));
            if ((i % 15) == 0) System.out.println ("");
        }
    }

    public void display ()
    {
        System.out.print("Player One: ");
        System.out.print(p1Name + "(" + p1ID + ") " + "[" + p1Score + "]");
        System.out.print("                      Player Two: ");
        System.out.println(p2Name + "(" + p2ID + ") " + "[" + p2Score + "]");
        if (PlayersTurnID == p1ID)
        {
            System.out.println(p1Name + "(P1) turn.");
        } else System.out.println(p2Name + "(P2) turn.");

        System.out.println("");

        displayBoard ();

        System.out.println("");

        if (PlayersTurnID == p1ID)
        {
            System.out.println(p1Name + "(P1)  Letters : " + p1Hand);
        } else System.out.println(p2Name + "(P2)  Letters : " + p2Hand);
    }

    public void setPlayerNames (String playerOne, String playerTwo)
    {
        p1Name = playerOne;
        p2Name = playerTwo;
    }

    public String getInput (String prompt)
    {
        String returnString = "";

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print (prompt);

        try {
            returnString = br.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return returnString;
    }

    public boolean checkPlayLetters (String playLetters)
    {
        boolean result = true;
        String checkAgainst = "";

        if (PlayersTurnID == p1ID)
        {
            checkAgainst = p1Hand;
        }
        else checkAgainst = p2Hand;

        int len = playLetters.length();
        for (int i = 0; i < len; i++)
        {
            int pos = checkAgainst.indexOf(playLetters.charAt(i));
            if (pos == -1) {
                // Letter not found in hand
                result = false;
            }
            else checkAgainst = checkAgainst.replaceFirst(String.valueOf(playLetters.charAt(i)),"");
        }

        return result;
    }

    public String removeFromHand (String removeTheseLetters)
    {
        String resultString;
        if (PlayersTurnID == p1ID)
        {
            resultString = p1Hand;
        }
        else resultString = p2Hand;

        int len = removeTheseLetters.length();
        for (int i = 0; i < len; i++)
        {
           resultString = resultString.replaceFirst(String.valueOf(removeTheseLetters.charAt(i)),"");
        }

        if (PlayersTurnID == p1ID)
        {
            p1Hand = resultString;
        }
        else p2Hand = resultString;

        return resultString;
    }


    public int letterValue (char letter)
    {
        int result = 0;
        int index = ((int) letter ) - 65;

        if ((index >= 0) && (index <= 25)) result = letterValues [index];

        return result;
    }


    public void nextPlayer () {
        if (PlayersTurnID == p1ID) {
            PlayersTurnID = p2ID;
        }
        else PlayersTurnID = p1ID;
        currentRound++;
    }
    public boolean getMove ()
    {
        boolean keepPlaying = true;
        String move = getInput("(P)ass, e(X)change, [M]ove : ");

        if (move.compareToIgnoreCase("P") == 0) {
            passCount++;

            if (passCount >= 3)
            {
                //Game over it is a draw
                System.out.println("Pass Limit Reached. Game Drawn.");
                keepPlaying = false;
            }

            nextPlayer ();
        }
        else if (move.compareToIgnoreCase("X") == 0) {
            String exchangeLetters = "";
            while (true) {
                exchangeLetters = getInput("Letters To Exchange : ");
                exchangeLetters = exchangeLetters.toUpperCase();
                // make sure letter are in hand
                if (checkPlayLetters(exchangeLetters) == true) {
                    if (exchangeLetters.length() <= letterBag.length()) {

                        break;
                    }
                    else System.out.print ("There are not enough letters in the bag to exchange.");

                }
                System.out.println ("");
            }

            removeFromHand (exchangeLetters);

            if (PlayersTurnID == p1ID) {
                dealLetters (exchangeLetters.length(), 1);
            }
            else dealLetters (exchangeLetters.length(), 2);

            letterBag = letterBag + exchangeLetters;

            passCount = 0;

            nextPlayer ();
        }
        else if (move.compareToIgnoreCase("M") == 0) {
            String playLetters = "";
            String direction = "";
            String from = "";
            while (true) {
                playLetters = getInput("Letters To Play : ");
                playLetters = playLetters.toUpperCase();

                System.out.println("");
            }
        }
        return keepPlaying;
    }
}