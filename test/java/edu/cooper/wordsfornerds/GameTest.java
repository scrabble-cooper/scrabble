package edu.cooper.wordsfornerds;
import edu.cooper.wordsfornerds.Game;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test Game class")
public class GameTest {

    @Test
    @DisplayName("Set and get p1ID successfully")
    public void testSetP1ID() {
        Game thisgame = new Game();
        thisgame.setP1ID(123);
        assertEquals(123, thisgame.getP1ID());
    }

    @Test
    @DisplayName("Set and get p2ID successfully")
    public void testSetP2ID() {
        Game thisgame = new Game();
        thisgame.setP2ID(456);
        assertEquals(456, thisgame.getP2ID());
    }

    @Test
    @DisplayName("Set and get p1Score successfully")
    public void testSetP1Score() {
        Game thisgame = new Game();
        thisgame.setP1Score(20);
        assertEquals(20, thisgame.getP1Score());
    }

    @Test
    @DisplayName("Set and get letter bag successfully")
    public void testSetLetterBag() {
        Game thisgame = new Game();
        thisgame.setLetterBag("ABC");
        assertEquals("ABC", thisgame.getLetterBag());
    }

    @Test
    @DisplayName("Set and get current round successfully")
    public void testSetCurrentRound() {
        Game thisgame = new Game();
        thisgame.setCurrentRound(1);
        assertEquals(1, thisgame.getCurrentRound());
    }

    @Test
    @DisplayName("Set and get p1Hand successfully")
    public void testSetP1Hand() {
        Game thisgame = new Game();
        thisgame.setP1Hand("ABC");
        assertEquals("ABC", thisgame.getP1Hand());
    }

    @Test
    @DisplayName("Set and get p2Hand successfully")
    public void testSetP2Hand() {
        Game thisgame = new Game();
        thisgame.setP2Hand("DEF");
        assertEquals("DEF", thisgame.getP2Hand());
    }

    @Test
    @DisplayName("Set and get winner successfully")
    public void testSetWinner() {
        Game thisgame = new Game();
        thisgame.setWinner(1);
        assertEquals(1, thisgame.getWinner());
    }

    @Test
    @DisplayName("Set and get pass count successfully")
    public void testSetPassCount() {
        Game thisgame = new Game();
        thisgame.setPassCount(1);
        assertEquals(1, thisgame.getPassCount());
    }

    @Test
    @DisplayName("Set and get pass turn ID successfully")
    public void testSetPlayersTurnID() {
        Game thisgame = new Game();
        thisgame.setPlayersTurnID(1);
        assertEquals(1, thisgame.getPlayersTurnID());
    }

    @Test
    @DisplayName("Get letter value of A successfully")
    public void testLetterValue1() {
        Game thisgame = new Game();
        int letterval = thisgame.letterValue('A');
        assertEquals(1, letterval);
        letterval = thisgame.letterValue('B');
        assertEquals(3, letterval);
        letterval = thisgame.letterValue('C');
        assertEquals(3, letterval);
        letterval = thisgame.letterValue('D');
        assertEquals(2, letterval);
        letterval = thisgame.letterValue('E');
        assertEquals(1, letterval);
        letterval = thisgame.letterValue('F');
        assertEquals(4, letterval);
        letterval = thisgame.letterValue('G');
        assertEquals(2, letterval);
        letterval = thisgame.letterValue('H');
        assertEquals(4, letterval);
        letterval = thisgame.letterValue('I');
        assertEquals(1, letterval);
        letterval = thisgame.letterValue('J');
        assertEquals(8, letterval);
        letterval = thisgame.letterValue('K');
        assertEquals(5, letterval);
        letterval = thisgame.letterValue('L');
        assertEquals(1, letterval);
        letterval = thisgame.letterValue('M');
        assertEquals(3, letterval);
        letterval = thisgame.letterValue('N');
        assertEquals(1, letterval);
        letterval = thisgame.letterValue('O');
        assertEquals(1, letterval);
        letterval = thisgame.letterValue('P');
        assertEquals(3, letterval);
        letterval = thisgame.letterValue('Q');
        assertEquals(10, letterval);
        letterval = thisgame.letterValue('R');
        assertEquals(1, letterval);
        letterval = thisgame.letterValue('S');
        assertEquals(1, letterval);
        letterval = thisgame.letterValue('T');
        assertEquals(1, letterval);
        letterval = thisgame.letterValue('U');
        assertEquals(1, letterval);
        letterval = thisgame.letterValue('V');
        assertEquals(4, letterval);
        letterval = thisgame.letterValue('W');
        assertEquals(4, letterval);
        letterval = thisgame.letterValue('X');
        assertEquals(8, letterval);
        letterval = thisgame.letterValue('Y');
        assertEquals(4, letterval);
        letterval = thisgame.letterValue('Z');
        assertEquals(10, letterval);
        letterval = thisgame.letterValue(' ');
        assertEquals(0, letterval);
        letterval = thisgame.letterValue('a');
        assertEquals(0, letterval);
    }

    @Test
    @DisplayName("Get letter value of B successfully")
    public void testLetterValue2() {
        Game thisgame = new Game();
        int letterval = thisgame.letterValue('B');
        assertEquals(3, letterval);
    }

    @Test
    @DisplayName("Get letter value of D successfully")
    public void testLetterValue3() {
        Game thisgame = new Game();
        int letterval = thisgame.letterValue('D');
        assertEquals(2, letterval);
    }

    @Test
    @DisplayName("Get letter value of Q successfully")
    public void testLetterValue4() {
        Game thisgame = new Game();
        int letterval = thisgame.letterValue('Q');
        assertEquals(10, letterval);
    }

    @Test
    @DisplayName("Get equal letter values successfully")
    public void testEqualLetterValue() {
        Game thisgame = new Game();
        int letterval = thisgame.letterValue('Q');
        int letterval2 = thisgame.letterValue('Z');
        assertEquals(letterval, letterval2);
    }

    @Test
    @DisplayName("Get non-equal letter values successfully")
    public void testNotEqualLetterValue() {
        Game thisgame = new Game();
        int letterval = thisgame.letterValue('A');
        int letterval2 = thisgame.letterValue('Z');
        assertNotEquals(letterval, letterval2);
    }

    @Test
    @DisplayName("Get non-equal letter values successfully")
    public void testSetBoard() {
        Game thisgame = new Game();
        thisgame.setBoard("ABC");
        assertEquals("ABC", thisgame.getBoard());
    }

    @Test
    @DisplayName("Set and Get P2Score")
    public void testSetAndGetP2Score() {
        Game thisgame = new Game();
        thisgame.setP2Score(978);
        assertEquals(978, thisgame.getP2Score());
    }

    @Test
    @DisplayName("Set and Get GameID")
    public void testSetAndGetGameId() {
        Game thisgame = new Game();
        thisgame.setGameId(99978);
        assertEquals(99978, thisgame.getGameId());
    }

}