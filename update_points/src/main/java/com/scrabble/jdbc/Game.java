package com.scrabble.jdbc;
import com.scrabble.jdbc.util.DataTransferObject;

public class Game implements DataTransferObject {

    private long p1ID;
    private long p2ID;
    private int p1Score;
    private int p2Score;

    public long getId() {
        return gameId;
    }

    public long getP1Id() {
        return p1ID;
    }
    
    public long getP2Id() {
        return p2ID;
    }

    //might not need this, since its already in Player.java
    // public void setPlayerId(long playerId) {
    //     this.playerId = playerId;
    // }

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
}