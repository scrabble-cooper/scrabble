package com.scrabblecooper.scrabble;

public class boardMSG {
    private int gameID;
    private String charToChange;
    private int boardRow;
    private int boardCol;
    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getCharToChange() {
        return charToChange;
    }

    public void setCharToChange(String charToChange) {
        this.charToChange = charToChange;
    }

    public int getBoardRow() {
        return boardRow;
    }

    public void setBoardRow(int boardRow) {
        this.boardRow = boardRow;
    }

    public int getBoardCol() {
        return boardCol;
    }

    public void setBoardCol(int boardCol) {
        this.boardCol = boardCol;
    }
}
