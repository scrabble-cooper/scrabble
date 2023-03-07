package com.scrabble.jdbc;
import com.scrabble.jdbc.util.DataTransferObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Array;
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

    private static Random rnd;
    private long gameId;
    private long p1ID;
    private long p2ID;
    private long PlayersTurnID; // ID of player whos turn it is
    private int p1Score;
    private int p2Score;
    private String letterBag;
    private int currentRound;
    private String p1Hand;
    private String p2Hand;
    private int winner;
    private Array board;
    private String[][] javaBoard;

    private String p1Name ; // Get from player record
    private String p2Name ;


    public long getGameId() {
        return gameId;
    }



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

    public Array getBoard() {
        return board;
    }

//    public void setBoard(String board) {
        public void setBoard(Array board) {

            board = board;
            Array sqlBoard = board;

            try {
                javaBoard = (String[][]) sqlBoard.getArray();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    public long getId() {
        return gameId;
    }

    private int dealLetters (int dealNLetters, long playerOneOrTwo) // 1 or 2
    {
        int  numberOfLettersLeft = letterBag.length();
        while ((numberOfLettersLeft > 1) && (dealNLetters > 0))
        {
            int i = rnd.nextInt(numberOfLettersLeft);

            System.out.println(letterBag.substring(i,i+1));

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

        for (int across = 0; across < boardWidth; across++) {
            System.out.print(" A" + across);
            if (across < 10) System.out.print(" ");
        }

        System.out.println("");

        for (int down = 0; down < boardDepth; down++) {
            if (down < 10) System.out.print(" ");
            System.out.print("D" + down +" ");

            for (int across = 0; across < boardWidth; across++) {
                System.out.print(javaBoard[down][across] + "  ");
            }
            System.out.println("");
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
            int pos = checkAgainst.indexOf(playLetters.substring (i,i));
            if (pos == -1) {
                // Letter no found in hand
                result = false;
            }
            else checkAgainst = checkAgainst.replaceFirst(playLetters.substring (i,i),"");
        }

        return result;
    }

    public void updateBoard (String withLetters, String direction, int fromDown, int fromAcross) {
        int len = withLetters.length();
        if (direction.compareTo ("V") == 0) {
            for (int i = 0; i < len; i++)
            {
                int down = fromDown + i ;
                String newLetter = " " + withLetters.charAt(i);
                javaBoard [down][fromAcross] = newLetter;
            }
        }
        else {
            for (int i = 0; i < len; i++)
            {
                int across = fromAcross + i ;
                javaBoard [fromDown][across] = withLetters.substring(i,i);
            }
        }
    }

    public void getMove ()
    {
        String move = getInput("(P)ass, e(X)change, [M]ove : ");

        if (move.compareToIgnoreCase("P") == 0) {


        }
        else if (move.compareToIgnoreCase("X") == 0) {

        }
        else {
            String playLetters = "";
            String direction   = "";
            String from        = "";
            while (true) {
                playLetters = getInput("Letters To Play : ");
                playLetters = playLetters.toUpperCase();
                if (checkPlayLetters(playLetters) == true) break;
                System.out.println ("");
            }

            while (true) {
                direction   = getInput  ("Direction (H(orizontal) or V(ertical) : " ); // H or V only
                direction = direction.toUpperCase();
                if ((direction.compareTo("V") == 0) || (direction.compareTo("H") == 0)) break;
                System.out.println ("");
            }

            while (true) {
                from = getInput("From (eg D7:A7) : ");
                from = from.toUpperCase();
                int pos = from.indexOf("D");
                if (pos == 0)
                {
                    pos = from.indexOf(":");
                    if ((pos == 2) || (pos == 3)) {
                        int anotherPos = from.indexOf("A");
                        if (anotherPos == pos + 1) {
                            String downStr   = from.substring (1,pos);
                            String acrossStr = from.substring (anotherPos+1);

                            int down = -1;
                            try {
                                down = Integer.parseInt(downStr);
                            }
                            catch (NumberFormatException e) {
                                down = -1;
                            }
                            if (down > -1)
                            {
                               int across = -1;
                                try {
                                    across = Integer.parseInt(acrossStr);
                                }
                                catch (NumberFormatException e) {
                                    across = -1;
                                }
                                if (across > -1 )
                                {
                                   updateBoard (playLetters, direction, down, across);
                                   displayBoard();
                                   break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}