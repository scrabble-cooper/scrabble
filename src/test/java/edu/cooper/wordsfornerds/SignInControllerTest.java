package edu.cooper.wordsfornerds;
import edu.cooper.wordsfornerds.SignIn;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestBody;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test SignInController  class")
public class SignInControllerTest {

    @Test
    @DisplayName("Register Failures. Name Too Long")
    public void testRegister() {
        SignInController thissignin = new SignInController();
        JSONObject jObjIn = new JSONObject();

        jObjIn.put("txName", "CharlesThe3rd");
        jObjIn.put("regname", "Deadducks"); // too long
        jObjIn.put("regpassword1", "CharlesThe3rd");
        jObjIn.put("regpassword2", "CharlesThe3rd");


        JSONObject jObjOut = new JSONObject();

        jObjOut.put("txData", "register");

        jObjOut.put("txName", "CharlesThe3rd");
        jObjOut.put("txData", "register");
        jObjOut.put("result", "false");
        jObjOut.put("response", "Name too long. Must be eight or fewer characters.");

        String result = null;
        try {
            result = thissignin.register(jObjIn.toString()).getContent();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(jObjOut.toString(), result );
    }
    @Test
    @DisplayName("Register Failures. Name Too Short")

    public void testRegister2() {
        SignInController thissignin = new SignInController();
        JSONObject jObjIn = new JSONObject();

        jObjIn.put("txName", "CharlesThe3rd");
        jObjIn.put("regname", "Dea"); // too short
        jObjIn.put("regpassword1", "CharlesThe3rd");
        jObjIn.put("regpassword2", "CharlesThe3rd");


        JSONObject jObjOut = new JSONObject();

        jObjOut.put("txData", "register");

        jObjOut.put("txName", "CharlesThe3rd");
        jObjOut.put("txData", "register");
        jObjOut.put("result", "false");
        jObjOut.put("response", "Name too short. Must be four or more characters.");

        String result = null;
        try {
            result = thissignin.register(jObjIn.toString()).getContent();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(jObjOut.toString(), result );
    }
    @Test
    @DisplayName("Register Failures. Invalid Characters")
    public void testRegister3() {
        SignInController thissignin = new SignInController();
        JSONObject jObjIn = new JSONObject();

        jObjIn.put("txName", "CharlesThe3rd");
        jObjIn.put("regname", "Dead du"); // has a space
        jObjIn.put("regpassword1", "CharlesThe3rd");
        jObjIn.put("regpassword2", "CharlesThe3rd");


        JSONObject jObjOut = new JSONObject();

        jObjOut.put("txData", "register");

        jObjOut.put("txName", "CharlesThe3rd");
        jObjOut.put("txData", "register");
        jObjOut.put("result", "false");
        jObjOut.put("response", "Name must be alphanumeric with no spaces.");

        String result = null;
        try {
            result = thissignin.register(jObjIn.toString()).getContent();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(jObjOut.toString(), result );
    }
    @Test
    @DisplayName("Register Failures. Passwords Not The Same")
    public void testRegister4() {
        SignInController thissignin = new SignInController();
        JSONObject jObjIn = new JSONObject();

        jObjIn.put("txName", "CharlesThe3rd");
        jObjIn.put("regname", "Deadduck");
        jObjIn.put("regpassword1", "Charles3rd");
        jObjIn.put("regpassword2", "CharlesThe3rd");


        JSONObject jObjOut = new JSONObject();

        jObjOut.put("txData", "register");

        jObjOut.put("txName", "CharlesThe3rd");
        jObjOut.put("txData", "register");
        jObjOut.put("result", "false");
        jObjOut.put("response", "Passwords are not the same.");

        String result = null;
        try {
            result = thissignin.register(jObjIn.toString()).getContent();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(jObjOut.toString(), result );
    }
    @Test
    @DisplayName("Register Failures. Password To Short")
    public void testRegister5() {
        SignInController thissignin = new SignInController();
        JSONObject jObjIn = new JSONObject();

        jObjIn.put("txName", "CharlesThe3rd");
        jObjIn.put("regname", "Deadduck");
        jObjIn.put("regpassword1", "Charl");
        jObjIn.put("regpassword2", "Charl");


        JSONObject jObjOut = new JSONObject();

        jObjOut.put("txData", "register");

        jObjOut.put("txName", "CharlesThe3rd");
        jObjOut.put("txData", "register");
        jObjOut.put("result", "false");
        jObjOut.put("response", "Password too short. Must be six or more characters.");

        String result = null;
        try {
            result = thissignin.register(jObjIn.toString()).getContent();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(jObjOut.toString(), result );
    }
    @Test
    @DisplayName("Register Failures. Password has Invalid Character")
    public void testRegister6() {
        SignInController thissignin = new SignInController();
        JSONObject jObjIn = new JSONObject();

        jObjIn.put("txName", "CharlesThe3rd");
        jObjIn.put("regname", "Deadduck");
        jObjIn.put("regpassword1", "Char les");
        jObjIn.put("regpassword2", "Char les");


        JSONObject jObjOut = new JSONObject();

        jObjOut.put("txData", "register");

        jObjOut.put("txName", "CharlesThe3rd");
        jObjOut.put("txData", "register");
        jObjOut.put("result", "false");
        jObjOut.put("response", "Password must be alphanumeric with no spaces.");

        String result = null;
        try {
            result = thissignin.register(jObjIn.toString()).getContent();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(jObjOut.toString(), result );
    }

    @Test
    @DisplayName("SignIn Failures. Invalid Username")
    public void testSignIn() {
        SignInController thissignin = new SignInController();
        JSONObject jObjIn = new JSONObject();

        jObjIn.put("txName", "CharlesThe3rd");
        jObjIn.put("name", "Dead uck");
        jObjIn.put("password", "Charl");


        JSONObject jObjOut = new JSONObject();

        jObjOut.put("txData", "register");

        jObjOut.put("txName", "CharlesThe3rd");
        jObjOut.put("txData", "signin");
        jObjOut.put("result", "false");
        jObjOut.put("response", "Username Invalid : Dead uck.");
        jObjOut.put ("user_name", "Dead uck");

        String result = null;
        try {
            result = thissignin.signin (jObjIn.toString()).getContent();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(jObjOut.toString(), result );
    }

}