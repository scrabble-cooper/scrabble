package com.scrabblecooper.scrabble;
import com.scrabblecooper.scrabble.util.DataTransferObject;

public class Player implements DataTransferObject {

    private long playerId;
    private String userName;
    private String password;
    private int totalGames;
    private int totalWin;
    private int totalLoss;
    private int totalTies;

    public long getId() {
        return playerId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public void setTotalGames(int totalGames) {
        this.totalGames = totalGames;
    }

    public int getTotalWin() {
        return totalGames;
    }

    public void setTotalWin(int totalWin) {
        this.totalWin = totalWin;
    }

    public int getTotalLoss() {
        return totalLoss;
    }

    public void setTotalLoss(int totalLoss) {
        this.totalLoss = totalLoss;
    }

    public int getTotalTies() {
        return totalTies;
    }

    public void setTotalTies(int totalTies) {
        this.totalTies = totalTies;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerId=" + playerId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}