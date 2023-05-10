package edu.cooper.wordsfornerds;
import edu.cooper.wordsfornerds.SignIn;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test SignIn class")
public class SignInTest {

    @Test
    @DisplayName("Get SignIn content successfully")
    public void testSignIn() {
        SignIn thissignin = new SignIn("testcontent");
        assertEquals("testcontent", thissignin.getContent());
    }
}