package com.scrabblecooper.scrabble;
import com.scrabble.jdbc.util.DataTransferObject;

import java.sql.Array;

public class Game implements DataTransferObject {

    private long gameId;
    private long p1ID;
    private long p2ID;
    private int p1Score;
    private int p2Score;
    private String letterBag;
    private int currentRound;
    private String p1Hand;
    private String p2Hand;
    private int winner;
    private Array board;
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
        this.board = board;
    }

    public long getId() {
        return gameId;
    }

}