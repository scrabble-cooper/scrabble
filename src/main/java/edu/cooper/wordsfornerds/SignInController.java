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

		if (registerPlayer.length() < 4)
		{
			jObj.put ("result", "false");
			jObj.put ("response", "Name too short. Must be four or more characters.");
		}
		else if (passwordOne.compareTo(passwordTwo) != 0)
		{
			jObj.put ("result", "false");
			jObj.put ("response", "Passwords are not the same.");
		}
		else if (passwordOne.length () < 8)
		{
			jObj.put ("result", "false");
			jObj.put ("response", "Password too short. Must be eight or more characters.");
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
		Long userid = jo.getLong("userid");
		String txName = jo.getString("txName");    // must always be present

		String Response = "UserID not found: " + userid + ".";

    	GameDAO gamedao = new GameDAO ();
		Long newGameID = gamedao.NewGame (userid);

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

		Long gameid = jo.getLong("gameID");

		JSONObject jGames = new JSONObject();

		jGames.put ("txData","joingame");
		jGames.put ("gameID",gameid);

		GameDAO gamedao = new GameDAO ();

		Long opponent = gamedao.joinGame ( gameid, joiningid );

		jGames.put ("txName",  txName);
		jGames.put ("opponnetId",opponent);
		return new SignIn (jGames.toString());
	}
	@MessageMapping("/refreshopponnent")
	@SendTo("/topic/greetings")
	public SignIn refreshopponnent (@RequestBody String message) throws Exception {
		Thread.sleep(100); // simulated delay

		JSONObject jo = new JSONObject(message);

		Long opponenetID = jo.getLong("opponenetid");
		String txName = jo.getString("txName");    // must always be present

		JSONObject jGames = new JSONObject();

		jGames.put ("txData","refreshopponnent");

		jGames.put ("txName",opponenetID);
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
}
