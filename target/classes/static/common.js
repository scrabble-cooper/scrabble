const characters ='ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';

function generateString(length) {
    let result = '';
    const charactersLength = characters.length;
    for ( let i = 0; i < length; i++ ) {
        result += characters.charAt(Math.floor(Math.random() * charactersLength));
    }

    return result;
}


function outputTitle (title,titleId ) { // The function will place a single row table of images spelling the title as child nodes of the node specified by id

   const myNode = document.getElementById(titleId);

   let tempHTML = "<table><tr><td>";

   for (let i = 0; i < title.length; i++) {
       let lcLetter = title[i].toLowerCase ();
       if (title[i] == " ") {
           tempHTML = tempHTML + "<img class=\"title-image\" src=\"images/clear.png\"  />";
       }
       else { tempHTML = tempHTML + "<img class=\"title-image\" src=\"images/" + lcLetter + ".svg\"  />"; }
   }

   tempHTML = tempHTML + "</td></tr></table>";

   myNode.innerHTML = tempHTML;
}

function createNodeFromHTML (htmlString) {
    var div = document.createElement('div');
    div.innerHTML = htmlString.trim();

    return div.firstChild;
}

function displayStartNewGameButton ()
{
   let myNode = document.getElementById("left-new-game");

   myNode.innerHTML = "<button onclick =\"sendCreateGame ()\" id=\"creategame\" class=\"btn btn-default\" >New Game</button>";
}


function displayWaitingForOpponent ()
{
   const myNode = document.getElementById("left-new-game");

   myNode.innerHTML = "<h2>Waiting for opponent to join game.</h2>";
}

function hideStartNewGameButton ()
{
   document.getElementById("creategame").hidden = true;
}
function showStartNewGameButton ()
{
   document.getElementById("creategame").hidden = false;
}



var stompClient = null;

function connect() {
    if (stompClient == null) {

        var socket = new SockJS('/gs-guide-websocket');
        stompClient = Stomp.over(socket);

        stompClient.connect({},
            function (frame) {
                console.log('Connected: ' + frame);
                stompClient.subscribe('/topic/greetings',
                   function (greeting) {
                      showResponse(JSON.parse(greeting.body).content);
                   }
                );
            }
        );
    }
}

function resetElementByID(theID) {
    document.getElementById(theID).reset();
}



function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function requestStatus() {
    //connect ();
    stompClient.send("/app/reqplayersstatus", {}, JSON.stringify({'name': userName,'txName':termID}));
}

function requestJoinableGames() {
    //connect ();
    stompClient.send("/app/joinables", {}, JSON.stringify({'user_id': userID,'txName':termID}));
}

function sendJoinGame (game_id) {
    stompClient.send("/app/joingame", {}, JSON.stringify({'user_id': userID, 'gameID': game_id,'txName':termID}));
}


function sendName() {
    //connect ();
    stompClient.send("/app/signin", {}, JSON.stringify({'name': $("#name").val(),'txName':termID, 'password': $("#password").val()}));
}

function sendCreateGame () {
    stompClient.send("/app/creategame", {}, JSON.stringify({'userid': userID,'txName':termID }));
}

function sendRefreshOpponnent (opponentID) {
    stompClient.send("/app/refreshopponnent", {}, JSON.stringify({'opponenetid': opponentID,'txName':termID }));
}

function sendRegistration() {
    //connect ();
    stompClient.send("/app/register", {},
                     JSON.stringify({'regname': $("#regname").val(),
                                     'regpassword1': $("#regpassword1").val(),
                                     'regpassword2': $("#regpassword2").val(),'txName':termID}
                         ));
}

function requestGameInfo (gameID)
{
    stompClient.send("/app/requestgameinfo", {},
                     JSON.stringify({'game_id': gameID,
                                     'user_id': userID,
                                     'txName':  termID}
                         ));

}

function showResponse(message) {
    const obj = JSON.parse(message);

    if (obj.txName == (termID))
    {
        // response is for this client it for this
        $("#response").innerHTML = "";
        const responseNode = document.getElementById("response");
        responseNode.appendChild (document.createTextNode(message));

        if ((obj.txData == "register") && (obj.result == "true")) {
           userName = $("#regname").val();
           resetElementByID("theForm");
           myRack = "         ";
           myRack = " WELCOME ";
           clearRack  ();
           displayRack();
           displayUserStatus ();
           //disconnect ();
          // window.location.replace("/gamePage.html?Player=" + username );
        }
        else if ((obj.txData == "signin") && (obj.result == "true")) {
           userName = $("#name").val();
           resetElementByID("theForm");
           myRack = "         ";
           myRack = " WELCOME ";
           clearRack  ();
           displayRack();
           displayUserStatus ();

           //disconnect ();
           //  window.location.replace("/gamePage.html?Player=" + username);
        }
        else {
           if (obj.txData == "reqplayersstatus")
           {
              userID = obj.userId;
              const objArr = obj.gamesInfo;
              let currentNumberOfGames = objArr.length;

              if (currentNumberOfGames == 0)
              {
                 // allow create new game
                 displayStartNewGameButton ();
              }
              else
              {
                 displayCurrentGames (objArr,currentNumberOfGames);
              }
              requestJoinableGames();
           }

           if (obj.txData == "creategame")
           {
              requestStatus();
           }

           if (obj.txData == "joingame")
           {
              if (obj.opponnetId > 0)
              {
                 currentGameID   = obj.gameID;
                 requestGameInfo (currentGameID);
                 sendRefreshOpponnent (obj.opponentId);
              }
           }

           if (obj.txData == "joinables")
           {
              const objArr = obj.gamesInfo;
              let currentJoinableGames = objArr.length;

              if (currentJoinableGames == 0)
              {
                 displayNoJoinableGames ();
              }
              else { displayJoinableGames (objArr,currentJoinableGames); }
           }

           if (obj.txData == "requestgameinfo")
           {
              if ((currentGameID <= 0) || ((currentGameID == obj.game_id)))
              {
                 boardStr = obj.board;
                 displayBoard(boardStr);
                 if (obj.p1_iD == userID)
                 {
                    myRack   = obj.p1_hand;

                 }
                 else {
                    myRack   = obj.p2_hand;
                 }
                 while (myRack.length < 9) myRack += " ";

                 clearRack();
                 displayRack ();

                 if (obj.players_turn_id == userID) { displayActionButtons (); }

              }
           }
        }
    }
    else if ((obj.txData == "refreshopponnent") && (obj.txName == userID)) { requestStatus (); }
}

function displayActionButtons ()
{
   let myNode = document.getElementById("action_tr");

  // myNode.innerHTML = "<h2>Challenge - Pass - Exchange - Play</h2>";
}

function displayNoJoinableGames ()
{
   let myNode = document.getElementById("left-joinable");

   myNode.innerHTML = "<h2>No Joinable Games</h2>";
}

function displayJoinableGames (objArr,count)
{
    let myNode = document.getElementById("left-joinable");
    myNode.innerHTML = "";

    let subhtml = "";

    for (let ii = 0; ii < count; ii++ ) {
        let row = objArr[ii];
        console.log (row);
        if ((row[0] > 0) && (row[2] < 1)) {
           subhtml += "<tr><td><button onclick =\"sendJoinGame (" + row['game_id'] + ")\" id=\"sendJoinGame\" class=\"btn btn-default\" >Join Game " + row['game_id'] + "</button></td></tr>";
        }
    }

    myNode.innerHTML = "<h2>Available Games To Join</h2><table>" + subhtml + "</table>";
}

function setAndShowCurrentGame (gameId)
{
   if (gameId > 0){
      currentGameID = gameId;
      requestGameInfo (currentGameID);
   }
}

function displayCurrentGames (objArr,count)
{
    let myNode = document.getElementById("left-current-games");
    myNode.innerHTML = "";

    let currentGameshtml = "";

    for (let ii = 0; ii < count; ii++ ) {
        let row = objArr[ii];

        if (row['p2_iD'] > 0) {
           // we have an active match
           if (row ['players_turn_id'] == userID)
           {
              //if (currentGameID <= 0) { currentGameID =  row['game_id']; }

              if (currentGameID == row['game_id'])
              {
                 currentGameshtml += "<tr class=\"highlight-game\"><td>My Turn</td><td onclick =\"setAndShowCurrentGame (" + row['game_id'] +")\">Game " + row['game_id'] + "<td></tr>";
              }
              else currentGameshtml += "<tr ><td>My Turn</td><td onclick =\"setAndShowCurrentGame (" + row['game_id'] +")\">Game " + row['game_id'] + "<td></tr>";

           }
           else { currentGameshtml += "<tr><td>Their Turn</td><td>Game " + row['game_id'] + "<td></tr>"; }
        }
        else { currentGameshtml += "<tr><td>Waiting for an Opponent</td><td>Game " + row['game_id'] + "</td></tr>";
        }
    }

    myNode.innerHTML = "<h2>Current Games</h2><table>" + currentGameshtml + "</table>";
}

/*
        left-login-status
            left-new-game
            left-joinable
            left-response
*/

$(function () {
    $("form").on('submit', function (e) { e.preventDefault(); });
    $( "#signin" ).click(function() { sendName(); });
    $( "#register" ).click(function() { sendRegistration(); });
//    $( "#creategame" ).click(function() { sendCreateGame(); });
});


function displayUserStatus () {

    if (userName != "")
    {
        const myNode = document.getElementById("left-login-status");

        myNode.innerHTML = "<h2>Welcome <b>" + userName + "</b></h2>" +

        "<button float=\"inline-end;\" onclick =\"displayUserStatus ( )\" id=\"displayGame\" class=\"btn btn-default\" >Refresh</button>";

        requestStatus();    //
    }
}

function displayBoard(words) {
    /*
       T = Triple Word Score
       D = Double Word Score
       B = Blank - no extra scores
       2 = Double Letter Score
       3 = Triple Letter Score
    */
    const mtBoardStr = "TBB2BBBTBBB2BBT" +
                       "BDBBB3BBB3BBBDB" +
                       "BBDBBB2B2BBBDBB" +
                       "2BBDBBB2BBBDBB2" +
                       "BBBBDBBBBBDBBBB" +
                       "B3BBB3BBB3BBB3B" +
                       "BB2BBB2B2BBB2BB" +
                       "TBB2BBBDBBB2BBT" +
                       "BB2BBB2B2BBB2BB" +
                       "B3BBB3BBB3BBB3B" +
                       "BBBBDBBBBBDBBBB" +
                       "2BBDBBB2BBBDBB2" +
                       "BBDBBB2B2BBBDBB" +
                       "BDBBB3BBB3BBBDB" +
                       "TBB2BBBTBBB2BBT";

    $("#board-body").innerHTML = "";
    $("#board-body").append("<tr>");

    for (let i = 0; i < 225; i++) {
        if (((i % 15) == 0) && (i > 0))
        {
            $("#board-body").append("</tr>");
            $("#board-body").append("<tr>");
         }

         if (words[i] != " ") {
            let lcLetter= words[i].toLowerCase();

            if (words[i] === lcLetter) {
               $("#board-body").append("<td class=\"board-wild-td\"><img class=\"board-wild-img\" src=\"images/" + lcLetter + ".svg\" /></td>");
            }
            else  {
               $("#board-body").append("<td><img class=\"board-image\" src=\"images/" + lcLetter + ".svg\" /></td>");
            }
         }
         else {
             switch(mtBoardStr[i]) {
                case "T": $("#board-body").append("<td id = \"board_td_" + i + "\" ondragover=\"allowDrop(event)\" ondrop=\"droponboard(event)\" class=\"triple-word\">Triple<br>Word</td>");
                    break;
                case "D": $("#board-body").append("<td id = \"board_td_" + i + "\" ondragover=\"allowDrop(event)\" ondrop=\"droponboard(event)\" class=\"double-word\">Double<br>Word</td>");
                    break;
                case "3": $("#board-body").append("<td id = \"board_td_" + i + "\"ondragover=\"allowDrop(event)\" ondrop=\"droponboard(event)\" class=\"triple-letter\">Triple<br>Letter</td>");
                    break;
                case "2": $("#board-body").append("<td id = \"board_td_" + i + "\" ondragover=\"allowDrop(event)\" ondrop=\"droponboard(event)\" class=\"double-letter\">Double<br>Letter</td>");
                    break;
                default:  $("#board-body").append("<td id = \"board_td_" + i + "\" ondragover=\"allowDrop(event)\" ondrop=\"droponboard(event)\" class=\"plain-score\"></td>");
            }
         }
    }
    $("#board-body").append("</tr>");
}

var dragFromID = "";


function allowDrop(ev) {
  ev.preventDefault();
}

function drag(ev) {
  //ev.dataTransfer.setData("text", ev.target.id);
  dragFromValue =  ev.target.value;
  dragFromID    =  ev.target.id;
}

function droponboard (ev) {
  ev.preventDefault();
  let str = "Rack to Board Drop from " + dragFromID + " to " + ev.target.id;
  console.log (str);

  let fromType       = dragFromID.substring (0,(dragFromID.length-1));
  let fromRackOffset = -1;
  let toType         = ev.target.id.substring (0,9);
  let toBoardOffset  = -1;

  if (fromType === "rack_img_")
  {
      // Dragging letter from rack
      fromRackOffset = dragFromID.substr (dragFromID.length-1);

      if (toType === "board_td_")
      {
         // dragging letter from rack to board
         toBoardOffset = ev.target.id.substr (9);

         // the objects stored in the following array will be boardIndex,rackindex, letter // uppercase not a wild card lowercase wild carded letter

         let lcLetter = myRack[fromRackOffset];
         let wildCardUsed = false;

         if (lcLetter === "*") {
            let   tempLetter = prompt ("Please enter Wild Card Letter", "");
            const aLetterRegEx = /^[a-zA-Z]{1}$/; // regex for a single alphabetic character any case
            //tempLetter.value.match(aLetterRegEx)
            if (true) { // XXXXXX neeed to work out why the match always returns undefined
               retractableLetters.push([toBoardOffset,fromRackOffset,lcLetter]);
               lcLetter = tempLetter.toLowerCase ();
               wildCardUsed = true;
            }
            else { return ; };
         }
         else {
            lcLetter = myRack[fromRackOffset].toLowerCase ();
            retractableLetters.push([toBoardOffset,fromRackOffset,lcLetter]);
         }

         /*
         I wanted to use substring but it always behaved weirdly when * are involved [RE TA IN*] caused issues never worked correctly when moving N

         let tempRack = myRack.substring (0,fromRackOffset);
         tempRack = tempRack + " ";
         console.log (myRack.substring (fromRackOffset+1,9))
         tempRack = tempRack + myRack.substring (fromRackOffset+1,9); //up to and excluding the last offset
         */

         let tempRack = "";
         for (let i = 0; i < 9; i++){
            if (i == fromRackOffset) {
               tempRack = tempRack + " ";
            }
            else { tempRack = tempRack + myRack [i]; }
         }

         myRack = tempRack;
         clearRack ();
         displayRack();

         // Now update the board

         const myNode = document.getElementById(ev.target.id);

         if (wildCardUsed) {
            myNode.innerHTML = "<td class=\"board-wild-td\"><img class=\"board-wild-img\" src=\"images/" + lcLetter + ".svg\" /></td>";

         }
         else { myNode.innerHTML = "<td><img class=\"board-image\" src=\"images/" + lcLetter + ".svg\" /></td>"; }
         //"<td class=\"board-wild-td\"><img class=\"board-wild-img\" src=\"images/" + lcLetter + ".svg\" /></td>");

         /*
         myRack = tempRack;
         clearRack ();
         displayRack();
      */

      }
  }
  dragFromID = "";
}

function droponrack (ev) {
  ev.preventDefault();
  let str = "Drag and Drop between " + dragFromID + " and " + ev.target.id;

  let fromType       = dragFromID.substring (0,(dragFromID.length-1));
  let fromRackOffset = -1;
  let toType         = ev.target.id.substring (0,(ev.target.id.length-1));
  let toRackOffset   = -1;
  let toBoardOffset  = -1;

  if (fromType === "rack_img_")
  {
      // Dragging letter from rack
      fromRackOffset = dragFromID.substr (dragFromID.length-1);

      if ((toType === "rack_img_") || (toType === "rack_td_"))
      {
         // dragging letter rack to rack
         toRackOffset = ev.target.id.substr (ev.target.id.length-1);

         if (toRackOffset != fromRackOffset)
         {
            // wow there is no replace character at index position within a string. What is this javascript!!!

            let tempRack = "";
            for (let i = 0; i < 9; i++)
            {
              if (i == toRackOffset) {
                 tempRack = tempRack + myRack [fromRackOffset];
              }
              else if (i == fromRackOffset) {
                 tempRack = tempRack + myRack [toRackOffset];
              }
              else {
                 tempRack = tempRack + myRack [i];
              }
            }

            myRack = tempRack;
            clearRack ();
            displayRack();
         }

      }
  }
  dragFromID = "";
}
function resetRack () {
   // moves letter back from the board to the rack
   /*
   while (retractableLetters.length > 0)
   {
       let temp = retractableLetters.pop ();
       temp [1]
   }

    = [];

            retractableLetters.push([toBoardOffset,fromRackOffset,lcLetter]);
   */
}

function clearRack ()
{
   // this functions removes the 9 <td> elements and children
   // due some slot are blank and not draggable it is simplest to delete all nine and then display them again
   document.getElementById("rack_0").remove();
   document.getElementById("rack_1").remove();
   document.getElementById("rack_2").remove();
   document.getElementById("rack_3").remove();
   document.getElementById("rack_4").remove();
   document.getElementById("rack_5").remove();
   document.getElementById("rack_6").remove();
   document.getElementById("rack_7").remove();
   document.getElementById("rack_8").remove();
}

function displayARackLetter (i) // an rack offset from 0 to 8
{
    // if time permits tidy up lowercase issue filenames to upper case letter

    let lcLetter = myRack[i].toLowerCase();

    if (lcLetter === " ") { // blank space in rack
        $("#rack_tr").append("<td ondragover=\"allowDrop(event)\" id=\"rack_" + i + "\" >" +
                             "<img id =\"rack_img_" + i + "\" class=\"rack-td\" ondrop=\"droponrack(event)\" src=\"images/clear.png\" /></td>");
    }
    else if (lcLetter === "*") { // wildcard in rack - you should do well
        $("#rack_tr").append("<td ondragover=\"allowDrop(event)\"  id=\"rack_" + i + "\" ondrop=\"droponrack(event)\">" +
                             "<img id =\"rack_img_" + i + "\" class=\"rack-td\"      src=\"images/*.svg\" draggable=\"true\" ondragstart=\"drag(event)\" /></td>");
    }
    else {
        $("#rack_tr").append ("<td ondragover=\"allowDrop(event)\"  id=\"rack_" + i + "\" class=\"rack-td\"  ondrop=\"droponrack(event)\">" +
                              "<img id =\"rack_img_" + i + "\" class=\"rack-img\" src=\"images/" + lcLetter + ".svg\" draggable=\"true\" ondragstart=\"drag(event)\" /></td>");
    }
}

function displayRack() {
    // should always be nine because it is padded with spaces. we want to be able to store the rack as sorted by the player
    // so even when a player has the maximum number of letters 7, there will be two spaces
    $("#rack_tr").textContent = '';

    for (let i = 0; i < 9; i++) displayARackLetter (i); // rack offset from 0 to 8

}

function resetElementByID(theID) {
    document.getElementById(theID).reset();
}




