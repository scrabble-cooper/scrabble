package edu.cooper.wordsfornerds;
import edu.cooper.wordsfornerds.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Test Player class")
public class PlayerTest {

    @Test
    @DisplayName("Set and get PlayerID successfully")
    public void testSetPlayerId() {
        Player thisplayer = new Player();
        thisplayer.setPlayerId(12345);
        assertEquals(12345, thisplayer.getPlayerId());
    }

    @Test
    @DisplayName("Set and get PlayerID successfully 2")
    public void testSetPlayerId2() {
        Player thisplayer = new Player();
        thisplayer.setPlayerId(12345);
        assertEquals(12345, thisplayer.getId());
    }

    @Test
    @DisplayName("Set and get username successfully")
    public void testSetUserName() {
        Player thisplayer = new Player();
        thisplayer.setUserName("RandomName");
        assertEquals("RandomName", thisplayer.getUserName());
    }

    @Test
    @DisplayName("To String")
    public void testSetPassword() {
        Player thisplayer = new Player();
        thisplayer.setUserName("Fred");
        thisplayer.setPassword("pAssWord");
        thisplayer.setPlayerId(362426);
        assertEquals("Player{playerId=362426, userName='Fred', password='pAssWord'}", thisplayer.toString());
    }
}