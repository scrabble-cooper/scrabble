package edu.cooper.wordsfornerds;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.HtmlUtils;
import org.springframework.boot.json.JsonParser;

import java.util.Map;

@Controller
public class SignInController {

	@MessageMapping("/signin")
	@SendTo("/topic/greetings")
	public SignIn signin (@RequestBody String message) throws Exception {
		Thread.sleep(100); // simulated delay

		JsonParser parser = JsonParserFactory.getJsonParser();
		Map<String,Object> pM = null;
		try {
			pM = parser.parseMap (message);
		}
		catch (Exception ex) { System.out.println ("Signin cannot parse JSON : " + message + ex); }
		String user_name = ((String) pM.get ("name")).trim();
		String password  = ((String) pM.get ("password")).trim();
		String txName    = ((String) pM.get ("txName")).trim();    // must always be present

		JSONObject jObj = new JSONObject();

		if (user_name.matches("^[a-zA-Z0-9]*$") == false)
		{
			// stop any sql escape character issues
			jObj.put ("result", "false");
			jObj.put ("response", "Username Invalid : " + user_name + ".");
		}
        else
		{
			PlayerDAO playerdao = new PlayerDAO ();
			Player player = playerdao.findByUsername(user_name);

			String str = player.getPassword(); // player is never null, but it may contain null data for user_name and password

			if (str != null) {
				if (str.compareTo(password) == 0) {
					jObj.put ("result", "true");
					jObj.put ("response", "Signedin: " + user_name + ".");

				} else {
					jObj.put ("result", "false");
					jObj.put ("response", "Incorrect Password for " + user_name + ".");
					// Should be same as user not found. i.e. Credentials not found.
					// but as we are new to this programming we want to see where and why fails
				}
			}
			else
			{
				jObj.put ("result", "false");
				jObj.put ("response", "Username not found: " + user_name + ".");
			}
		}
		jObj.put ("txName", txName);
		jObj.put ("txData","signin");
		jObj.put ("user_name", user_name);

		System.out.println (jObj.toString() );
		return new SignIn (jObj.toString());
	}

	@MessageMapping("/register")
	@SendTo("/topic/greetings")
	public SignIn register(@RequestBody String message) throws Exception {
		Thread.sleep(100); // simulated delay
		JsonParser parser = JsonParserFactory.getJsonParser();
		Map<String,Object> pM = null;
		String Response = "";
		try {
		    pM = parser.parseMap (message);
		}
		catch (Exception ex) { System.out.println ("Register cannot parse JSON : " + message + ex); }

		// This is a quick workaround. To keep moving forward will fix if need be.
		// Probably once a User has signed in then switch to directing messages to
		// user-id only and not broadcast. This means changes to Controller but want to finish game
		// first. I do not expect we will be swamping the internet or cooper union with scrabble
		// broadcast traffic

		String txName         = ((String) pM.get ("txName")).trim();    // must always be present
		String registerPlayer = ((String) pM.get ("regname")).trim();
		String passwordOne    = ((String) pM.get ("regpassword1")).trim();
		String passwordTwo    = ((String) pM.get ("regpassword2")).trim();

		JSONObject jObj = new JSONObject();

		if (registerPlayer.length() >8)
		{
			jObj.put ("result", "false");
			jObj.put ("response", "Name too long. Must be eight or fewer characters.");
		}
		else if (registerPlayer.length() < 4)
		{
			jObj.put ("result", "false");
			jObj.put ("response", "Name too short. Must be four or more characters.");
		}
		else if (registerPlayer.matches("^[a-zA-Z0-9]*$") == false)
		{
			jObj.put ("result", "false");
			jObj.put ("response", "Name must be alphanumeric with no spaces.");
		}
		else if (passwordOne.compareTo(passwordTwo) != 0)
		{
			jObj.put ("result", "false");
			jObj.put ("response", "Passwords are not the same.");
		}
		else if (passwordOne.length () < 6)
		{
			jObj.put ("result", "false");
			jObj.put ("response", "Password too short. Must be six or more characters.");
		}
		else if (passwordOne.matches("^[a-zA-Z0-9]*$") == false)
		{
			jObj.put ("result", "false");
			jObj.put ("response", "Password must be alphanumeric with no spaces.");
		}
		else
		{
			PlayerDAO playerdao = new PlayerDAO ();
			jObj = playerdao.createPlayer (registerPlayer,passwordOne);
		}

		jObj.put ("txName", txName);
		jObj.put ("txData","register");

		System.out.println (jObj.toString() );
		return new SignIn (jObj.toString());
	}
	@MessageMapping("/reqplayersstatus")
	@SendTo("/topic/greetings")
	public SignIn reqStatus (@RequestBody String message) throws Exception {
		Thread.sleep(100); // simulated delay

		JsonParser parser = JsonParserFactory.getJsonParser();
		Map<String,Object> pM = null;
		try {
			pM = parser.parseMap (message);
		}
		catch (Exception ex) { System.out.println ("Reque cannot parse JSON : " + message + ex); }
		String user_name = ((String) pM.get ("name")).trim();
		String Response = "Username not found: " + user_name + ".";
		String txName   = ((String) pM.get ("txName")).trim();    // must always be present
		PlayerDAO playerdao = new PlayerDAO ();
		Player player = playerdao.findByUsername(user_name);

		long userid = player.getPlayerId(); // player is never null, but it may contain null data for user_name and password
		JSONObject jUser = new JSONObject();

		jUser.put ("txName",txName);
		jUser.put ("txData","reqplayersstatus");
		jUser.put ("username",player.getUserName());
		jUser.put ("userId",player.getId());

		JSONArray obj = null;

		if (userid != 0) {
			// get current games in progress games
			GameDAO gamedao = new GameDAO ();
			obj = gamedao.findByPlayerId (userid);
			jUser.put ("gamesInfo",obj);
		}

		return new SignIn (jUser.toString());
	}

	@MessageMapping("/creategame")
	@SendTo("/topic/greetings")
	public SignIn createGame (@RequestBody String message) throws Exception {
		Thread.sleep(100); // simulated delay

		JSONObject jo = new JSONObject(message);
		//result.id = jo.getLong("comment_id");
//		JsonParser parser = JsonParserFactory.getJsonParser();
//		Map<String,Object> pM = null;
//		try {
//			pM = parser.parseMap (message);
		//}
		//catch (Exception ex) { System.out.println ("CreateGame cannot parse JSON : " + message + ex); }
		//Long userid = ((Long) pM.get ("userid"));
		Long userid   = jo.getLong("userid");
		String username = jo.getString("username");
		String txName = jo.getString("txName");    // must always be present

		String Response = "UserID not found: " + userid + ".";

    	GameDAO gamedao = new GameDAO ();
		Long newGameID = gamedao.NewGame (userid,username);

		JSONObject jUser = new JSONObject();

		jUser.put ("txName",txName);
		jUser.put ("txData","creategame");
		jUser.put ("gameid",newGameID);

		return new SignIn (jUser.toString());
	}


	@MessageMapping("/joinables")
	@SendTo("/topic/greetings")
	public SignIn joinableGames (@RequestBody String message) throws Exception {
		Thread.sleep(100); // simulated delay

		JSONObject jo = new JSONObject(message);

		Long userid = jo.getLong("user_id");
		String txName = jo.getString("txName");    // must always be present

		String Response = "UserID not found: " + userid + ".";

		GameDAO gamedao = new GameDAO ();
		JSONArray obj = gamedao.joinableGames (userid);

		JSONObject jGames = new JSONObject();

		jGames.put ("txName",txName);
		jGames.put ("txData","joinables");
		jGames.put ("gamesInfo",obj);

		return new SignIn (jGames.toString());
	}

	@MessageMapping("/joingame")
	@SendTo("/topic/greetings")
	public SignIn joingame (@RequestBody String message) throws Exception {
		Thread.sleep(100); // simulated delay

		JSONObject jo = new JSONObject(message);

		Long joiningid = jo.getLong("user_id");
		String txName = jo.getString("txName");    // must always be present
		String joiningUsername = jo.getString("username");    // must always be present

		Long gameid = jo.getLong("gameID");

		JSONObject jGames = new JSONObject();

		jGames.put ("txData","joingame");
		jGames.put ("game_id",gameid);

		GameDAO gamedao = new GameDAO ();

		Long opponent_id = gamedao.joinGame ( gameid, joiningid,joiningUsername );

		jGames.put ("txName",  txName);
		jGames.put ("opponent_id",opponent_id);
		return new SignIn (jGames.toString());
	}
	@MessageMapping("/refreshopponnent")
	@SendTo("/topic/greetings")
	public SignIn refreshopponnent (@RequestBody String message) throws Exception {
		Thread.sleep(100); // simulated delay

		JSONObject jo = new JSONObject(message);

		Long opponentID = jo.getLong("opponent_id");
		String txName = jo.getString("txName");    // must always be present

		JSONObject jGames = new JSONObject();

		jGames.put ("txData","refreshopponnent");

		jGames.put ("txName",opponentID);
		return new SignIn (jGames.toString());
	}

	@MessageMapping("/requestgameinfo")
	@SendTo("/topic/greetings")
	public SignIn requestGameInfo (@RequestBody String message) throws Exception {
		Thread.sleep(100); // simulated delay

		JSONObject jo = new JSONObject(message);

		Long gameID   = jo.getLong   ("game_id");
		String txName = jo.getString ("txName");    // must always be present

		GameDAO gamedao = new GameDAO ();

		JSONObject jGames = gamedao.findById (gameID);

		jGames.put ("txData","requestgameinfo");
		jGames.put ("txName",txName);

		return new SignIn (jGames.toString());
	}

	@MessageMapping("/moveplay")
	@SendTo("/topic/greetings")
	public SignIn movePlay (@RequestBody String message) throws Exception {
		Thread.sleep(100); // simulated delay

		JSONObject jo = new JSONObject(message);

		Long gameID   = jo.getLong   ("game_id");
		String txName = jo.getString ("txName");    // must always be present

		GameDAO gamedao = new GameDAO ();

		gamedao.movePlay (message);

		JSONObject jGames = gamedao.findById (gameID);

		jGames.put ("txData","moveplay");
		jGames.put ("game_id",gameID);
		jGames.put ("txName",txName);

		return new SignIn (jGames.toString());

	}


	@MessageMapping("/wordquery")
	@SendTo("/topic/greetings")
	public SignIn wordQuery (@RequestBody String message) throws Exception {
		Thread.sleep(100); // simulated delay

		JSONObject jo = new JSONObject(message);

		String wordList = jo.getString ("wordlist");

		DictionaryDAO dictionaryDAO = new DictionaryDAO();

		JSONArray obj = dictionaryDAO.checkWords(wordList);

		JSONObject jReturn = new JSONObject();

		jReturn.put ("dictionary_response",obj);
		jReturn.put ("txData","wordquery");
		jReturn.put ("user_id",jo.getLong   ("user_id"));
		jReturn.put ("game_id",jo.getLong   ("game_id"));
		jReturn.put ("txName", jo.getString ("txName"));

		return new SignIn (jReturn.toString());

	}

	@MessageMapping("/lastmovemakepermanent")
	@SendTo("/topic/greetings")
	public SignIn lastMoveMakePermanent (@RequestBody String message) throws Exception {
		Thread.sleep(100); // simulated delay

		JSONObject jo = new JSONObject(message);

		Long gameID     = jo.getLong   ("game_id");
		String txName   = jo.getString ("txName");    // must always be present
		Long userID     = jo.getLong   ("user_id");

		GameDAO gamedao = new GameDAO();

		gamedao.lastMoveMakePermanent(message);

		JSONObject jReturn = new JSONObject();

		jReturn.put ("txData","lastmovemakepermanent");
		jReturn.put ("user_id",userID);
		jReturn.put ("game_id",gameID);
		jReturn.put ("txName",txName);

		return new SignIn (jReturn.toString());

	}


	@MessageMapping("/unmakelastmove")
	@SendTo("/topic/greetings")
	public SignIn unMakeLastMove (@RequestBody String message) throws Exception {
		Thread.sleep(100); // simulated delay

		JSONObject jo = new JSONObject(message);

		Long gameID     = jo.getLong   ("game_id");
		String txName   = jo.getString ("txName");    // must always be present
		Long userID     = jo.getLong   ("user_id");

		GameDAO gamedao = new GameDAO();

		gamedao.unMakeLastMove(message);

		JSONObject jReturn = new JSONObject();

		jReturn.put ("txData","unmakelastmove");
		jReturn.put ("user_id",userID);
		jReturn.put ("game_id",gameID);
		jReturn.put ("txName",txName);

		return new SignIn (jReturn.toString());

	}





	@MessageMapping("/movepass")
	@SendTo("/topic/greetings")
	public SignIn movePass (@RequestBody String message) throws Exception {
		Thread.sleep(100); // simulated delay

		JSONObject jo = new JSONObject(message);

		Long gameID     = jo.getLong   ("game_id");
		String txName   = jo.getString ("txName");    // must always be present
		Long userID     = jo.getLong   ("user_id");

		GameDAO gamedao = new GameDAO();

		gamedao.movePass(message);

		JSONObject jReturn = new JSONObject();

		jReturn.put ("txData","movepass");
		jReturn.put ("user_id",userID);
		jReturn.put ("game_id",gameID);
		jReturn.put ("txName",txName);

		return new SignIn (jReturn.toString());

	}

	@MessageMapping("/moveexchange")
	@SendTo("/topic/greetings")
	public SignIn moveExchange (@RequestBody String message) throws Exception {
		Thread.sleep(100); // simulated delay

		JSONObject jo = new JSONObject(message);

		Long gameID     = jo.getLong   ("game_id");
		String txName   = jo.getString ("txName");    // must always be present
		Long userID     = jo.getLong   ("user_id");

		GameDAO gamedao = new GameDAO();

		gamedao.moveExchange(message);

		JSONObject jReturn = new JSONObject();

		jReturn.put ("txData","moveexchange");
		jReturn.put ("user_id",userID);
		jReturn.put ("game_id",gameID);
		jReturn.put ("txName",txName);

		return new SignIn (jReturn.toString());

	}

}

