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

   title = title.toUpperCase ();
   const myNode = document.getElementById(titleId);

   let tempHTML = "<table><tr><td>";

   tempHTML += "<td><img class=\"title-image\"  src=\"images/CooperUnion.png\"  /></td>"
   tempHTML += "<td>ECE366 - Spring 2023<br >Software Engineering &<br > Large Systems Design&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>";

   tempHTML += "<td>";
  // tempHTML += "<img class=\"title-image\" src=\"images/clear.png\"  />";

   for (let i = 0; i < title.length; i++) {
       if (title[i] == " ") {
           tempHTML = tempHTML + "<img class=\"title-image\" src=\"images/clear.png\"  />";
       }
       else { tempHTML = tempHTML + "<img class=\"title-image\" src=\"images/" + title[i] + ".svg\"  />"; }
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
var socket      = null;

function lostComms ()
{
   console.log('lost comms called.');

   document.body.innerHTML = "<H1><br><br>The Server Closed Communications.<br><br>Go do something constructive!!! In fresh air maybe.</H1>";
  // Restart from beginning
//   stompClient.disconnect();
  // setConnected();
}


function connect() {
    if (stompClient == null) {

        socket = new SockJS('/gs-guide-websocket');
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

        socket.onclose = function() {
            console.log('close');
            lostComms ();
        };

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
    stompClient.send("/app/joingame", {}, JSON.stringify({'user_id': userID, 'username': userName, 'gameID': game_id,'txName':termID}));
}


function sendName() {
    //connect ();
    stompClient.send("/app/signin", {}, JSON.stringify({'name': $("#name").val(),'txName':termID, 'password': $("#password").val()}));
}

function sendCreateGame () {
    stompClient.send("/app/creategame", {}, JSON.stringify({'userid': userID,'username': userName,'txName':termID }));
}

function sendRefreshOpponnent (opponentID) {
    stompClient.send("/app/refreshopponnent", {}, JSON.stringify({'opponentid': opponentID,'txName':termID }));
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

    console.log (obj);

    if (obj.txName == (termID))
    {
        // response is for this client it for this
        $("#response").innerHTML = "";
        const responseNode = document.getElementById("response");
        //responseNode.appendChild (document.createTextNode(message));
        if (obj.txData == "register") {
            if (obj.result == "true") {
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
            else {  responseNode.innerHTML = obj.response; }
        } else
        if (obj.txData == "signin") {
            if (obj.result == "true") {
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
            else {  responseNode.innerHTML = obj.response; }
        } else
        if (obj.txData == "reqplayersstatus")
        {
           userID = obj.userId;
           const objArr = obj.gamesInfo;
           let currentNumberOfGames = objArr.length;

           if (currentNumberOfGames == 0) {
             // allow create new game
              displayStartNewGameButton ();
           }
           else {
             displayCurrentGames (objArr,currentNumberOfGames);
           }
           requestJoinableGames();
        } else
        if (obj.txData == "creategame")
        {
          requestStatus();
        } else
        if (obj.txData == "joingame")
        {
           currentGameID   = obj.game_id;
           requestGameInfo (currentGameID);
        } else
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
        else
        {
           // this else down processes the right hand side of the scrabble web page
           if (obj.txData == "wordquery")
           {
              if (displayWordQueryResponse (obj.dictionary_response,obj.dictionary_response.length)) {
                 // Challenge was unsuccessful
                  makeLastMovePermanent ();
               } else {
                   unMakeLastMove ();


               }
           }
           if (obj.txData == "requestgameinfo")
           {
              if ((currentGameID <= 0) || ((currentGameID == obj.game_id)))
              {
                 currentGame = obj;
                 myExchange  = "         ";

                 currentGameID == obj.game_id;

                 if (obj.p1_iD == userID) { myRack   = obj.p1_hand; } else { myRack   = obj.p2_hand; }
                 while (myRack.length < 9) myRack += " ";

                 clearRack();
                 displayRack ();

                 currentGame.GameBoard = new board (bNode, 15, 225, scrabbleBk);

                 currentGame.GameBoard.appendBoard (obj.board, obj.lastplay, obj.playedby); // this is the setter
                                                                                            //there is no such thing as GameBoard.lastplay it i private same for played by and board getters and setters
              }
           }

           updatePlayersView ();
        }
    }
    else
    {
       if (obj.game_id == currentGameID) {
           switch(obj.txData) {
              case "lastmovemakepermanent":
                         requestGameInfo (currentGameID);
                         break;
              case "unmakelastmove":
                         requestGameInfo (currentGameID);
                         break;

              case "movePlay":
                         requestGameInfo (currentGameID);
                         break;
              case "movePass":
                         requestGameInfo (currentGameID);
                         break;
              case "moveExchange":
                         requestGameInfo (currentGameID);
                         break;
              case "wordquery":
                      displayWordQueryResponse (obj.dictionary_response,obj.dictionary_response.length);
                      break;
              default:
          }
       }
       else if (currentGameID == -1) {
          // I am idle not looking at another game and someone joined one of my open games switch to that game
          if ((myListOfGameIDs.indexOf(obj.game_id) != -1 ) && (obj.txData == "joingame"))
          {
             console.log (obj.opponent_id + " " + userID);
             //CurrentGameID = obj.game_id;
             requestGameInfo (obj.game_id);
          }


       }

//       if ((obj.txData == "refreshopponnent") && (obj.txName == userID)) { requestStatus (); }
    }
}

function unMakeLastMove ()
{
    // Change the challenged players hand back ie add back last played letters

    if (currentGame.players_turn_id == currentGame.p1_iD) {
        currentGame.p2_hand = currentGame.p2_hand.replaceAll (" ","") + " ";
        currentGame.p2_hand += currentGame.GameBoard.clearAndReturnLastPlayed ();
    }
    else {
        currentGame.p1_hand = currentGame.p1_hand.replaceAll (" ","") + " ";
        currentGame.p1_hand += currentGame.GameBoard.clearAndReturnLastPlayed ();
    } ;

   // if challenge count == 1 i.e. two failed challenges reset opponents move and they skip their turn
   // if challenge count == 0 i.e. one failed challenges reset opponents move and they go again

   if (currentGame.challengecount == 0) {
      if (currentGame.players_turn_id == currentGame.p1_iD) {
          currentGame.players_turn_id = currentGame.p2_iD;
      }
      else currentGame.players_turn_id = currentGame.p1_iD;
      currentGame.challengecount += 1;
   } else {
      currentGame.current_round += 1;
      currentGame.challengecount = 0;
   }

   updatePlayersView ();

   stompClient.send("/app/unmakelastmove", {},        // in response to a successful chalenge
       JSON.stringify({'game_id'         : currentGameID,
                       'user_id'         : userID,

                       'p1_hand'         : currentGame.p1_hand,
                       'p2_hand'         : currentGame.p2_hand,

                       'current_round'   : currentGame.current_round,
                       'challengecount'  : currentGame.challengecount,

                       'players_turn_id' : currentGame.players_turn_id,

                       'lastplay'        : currentGame.GameBoard.getLastPlay(),
                       'txName'          : termID}
      ));


}



function accept ()
{
   makeLastMovePermanent ();

//   updatePlayersView ();
}

function updatePlayersView ()
{
    if (currentGame.p1_iD == userID) {
       myRack   = currentGame.p1_hand;
    } else {
       myRack   = currentGame.p2_hand;
    }

    while (myRack.length < 9) myRack += " ";

    myExchange = "         ";
    clearRack();
    displayRack ();

    currentGame.GameBoard.appendBoard ();

    if ((currentGameID != 0) && (currentGame.GameBoard.getLastPlay () != blankStart))
    {
       let score = currentGame.GameBoard.scorePlayedLetters ();

       showChallengeDiv (currentGame.GameBoard.retrieveNewWords());
    }
    else hideChallengeDiv ();

    showPlayerScores ();

    showNumberOfBagLetters ();

    if (currentGame.players_turn_id == userID) {
       enableActionButtons ();
    }
    else  disableActionButtons ();
}


function makeLastMovePermanent ()
{
   let player_number = 1;
   if (currentGame.players_turn_id == currentGame.p1_iD){
      // it is player ones turn so the last move was by player 2
      player_number = 2;
   }

   //currentGame.lastplay
   let lettersPlayed = currentGame.GameBoard.updateBoardWithLastPlayed (player_number);

   if (player_number == 2) {
      currentGame.p2_score += currentGame.GameBoard.retrieveMovePoints ();

      currentGame.p2_hand = currentGame.p2_hand.replace(/\s+/g, '');

      for (let i = 0; i < lettersPlayed.length; i++ ) currentGame.p2_hand = currentGame.p2_hand.replace(lettersPlayed [i], '');

      currentGame.p2_hand += dealLetters (7 - currentGame.p2_hand.length);
   }
   else {
      currentGame.p1_score += currentGame.GameBoard.retrieveMovePoints ();

      currentGame.p1_hand = currentGame.p1_hand.replace(/\s+/g, '');

      for (let i = 0; i < lettersPlayed.length; i++ ) currentGame.p1_hand = currentGame.p1_hand.replace(lettersPlayed [i], '');

      currentGame.p1_hand += dealLetters (7 - currentGame.p1_hand.length);
   }

   currentGame.current_round += 1;
   currentGame.passcount      = 0;
   currentGame.challengecount = 0;

   updatePlayersView ();

   stompClient.send("/app/lastmovemakepermanent", {},
       JSON.stringify({'game_id'         : currentGameID,
                       'user_id'         : userID,
                       'p1_score'        : currentGame.p1_score,
                       'p2_score'        : currentGame.p2_score,

                       'p1_hand'         : currentGame.p1_hand,
                       'p2_hand'         : currentGame.p2_hand,

                       'current_round'   : currentGame.current_round,
                       'passcount'       : currentGame.passcount,
                       'challengecount'  : currentGame.challengecount,

                       'letters_left'    : currentGame.letters_left,

                       'board'           : currentGame.GameBoard.getTheWords(),
                       'playedby'        : currentGame.GameBoard.getPlayedBy(),
                       'lastplay'        : currentGame.GameBoard.getLastPlay(),
                       'txName'          : termID}
      ));
}

function dealLetters (dealNLetters)
{
    let result = "";
    let letterBag = currentGame.letters_left;

    while ((currentGame.letters_left.length > 0) && (dealNLetters > 0))
    {
        let i = Math.floor(Math.random() * currentGame.letters_left.length);

        result += currentGame.letters_left[i];

        if (i == 0)
        {
            letterBag = currentGame.letters_left.substring (1);
        }
        else letterBag = currentGame.letters_left.substring (0,i) + currentGame.letters_left.substring (i+1);

        currentGame.letters_left = letterBag;

        dealNLetters--;
    }
    return result;
}

function countOfNonSpaceLetters (aStr)
{
   let tempStr = aStr.replaceAll (" ", "");

   return tempStr.length;
}

function showNumberOfBagLetters ()
{
   let myNode = document.getElementById("letters_div");

   //debug only remove before publishing

   let tempWC = currentGame.letters_left.length + countOfNonSpaceLetters (currentGame.p1_hand) + countOfNonSpaceLetters (currentGame.p2_hand)
       + countOfNonSpaceLetters (currentGame.GameBoard.getLastPlay()) + countOfNonSpaceLetters (currentGame.GameBoard.getTheWords());

   let tempStr =  " r1 " + countOfNonSpaceLetters (currentGame.p1_hand);
       tempStr += " r2 " + countOfNonSpaceLetters (currentGame.p2_hand);
       tempStr += " lp " + countOfNonSpaceLetters (currentGame.GameBoard.getLastPlay());
       tempStr += " mb " + countOfNonSpaceLetters (currentGame.GameBoard.getTheWords());
       tempStr += " 100 == " + tempWC;
       tempStr += " pc " + currentGame.passcount;
       tempStr += " cc " + currentGame.challengecount;

   myNode.innerHTML =  tempStr + "<br>" + currentGame.letters_left.length + " : <meter title=\"Letters left to be dealt.\" id=\"letters_dealt\" value=\"" + currentGame.letters_left.length + "\" min=\"0\" max=\"100\"></meter>";
}

function showPlayerScores ()
{
   let myNode = document.getElementById("player_one_score");

   let temp = "";

   if (currentGame.players_turn_id == currentGame.p1_iD) {
      temp = " ⏪";
   } else temp = "";

   myNode.innerHTML =  currentGame.p1_username + "&nbsp;&nbsp;" + currentGame.p1_score + temp;

   myNode = document.getElementById("game_round");
   myNode.innerHTML = currentGameID  + "." + currentGame.current_round ;

   if (currentGame.players_turn_id == currentGame.p2_iD) {
      temp = "⏩ ";
   } else temp = "";

   myNode = document.getElementById("player_two_score");
   myNode.innerHTML = temp + currentGame.p2_score + "&nbsp;&nbsp;" + currentGame.p2_username ;
}

function clearWordQueryResponse ()
{
   let myNode = document.getElementById("word_response_table");
   myNode.innerHTML = "";
}

function displayWordQueryResponse (response,count )
{
   result = true; // challenged failed words are all good
   let myNode = document.getElementById("word_response_table");

   if (count > 0)
   {
      let tempStr = "";
      for (let i = 0; i < count; i++ )
      {
         let row = response [i];

         tempStr += "<tr><td class=\"played_word\">";
         tempStr += row ['word'];
         tempStr += "<td class=\"played_comment\">";
         tempStr +=  row ['definition'];
         tempStr += "</td></tr>";

         if (row ['definition'] == "Oops!. Not in Dictionary.") result = false; // The challenge was successful
      }
      myNode.innerHTML = tempStr;
   }
   else myNode.innerHTML = "";
   return result;
}


function refreshCurrent ()
{
 if (currentGame.game_id >=0) {
     currentGame.GameBoard.appendBoard ();

  }
}
function moveExchange ()
{
   clearWordQueryResponse ();

   if (myExchange == "         ") {
       alert ("Double click on the letters you want to exchange first!!!!!.")
   }
   else
   {
      let tempHand  = "";
      let forTheBag = ""; // the letters to be returned to the bag

      for (let i = 0; i < 9; i++)  {
         if ((myExchange [i] == " ") && (myRack [i] != " ")) tempHand += myRack [i];
         if  (myExchange [i] != " ") forTheBag += myRack [i];
      }

      if (currentGame.players_turn_id == currentGame.p1_iD) {
         currentGame.p1_hand = tempHand + dealLetters (7 - tempHand.length);
         currentGame.players_turn_id = currentGame.p2_iD;
      }
      else {
         currentGame.p2_hand = tempHand + dealLetters (7 - tempHand.length);
         currentGame.players_turn_id = currentGame.p1_iD;
      }

      currentGame.current_round++;
      currentGame.letters_left += forTheBag ;

      stompClient.send("/app/moveexchange", {},
           JSON.stringify({'game_id'         : currentGameID,
                           'user_id'         : userID,
                           'current_round'   : currentGame.current_round,
                           'p1_hand'         : currentGame.p1_hand,
                           'p2_hand'         : currentGame.p2_hand,
                           'letters_left'    : currentGame.letters_left,
                           'players_turn_id' : currentGame.players_turn_id,
                           'txName'          : termID}
       ));

       updatePlayersView ();



   }
}

function movePass ()
{
    clearWordQueryResponse ();

    let   tempReply = prompt ("PASS!!! Are you really sure? type Y to confirm.", "NO");

    if ((tempReply.toUpperCase () == "Y") || (tempReply.toUpperCase () == "YES"))
    {
        resetRack ();

        if (currentGame.players_turn_id == currentGame.p1_iD) { currentGame.players_turn_id = currentGame.p2_iD; }
        else { currentGame.players_turn_id = currentGame.p1_iD;  }

        currentGame.current_round = currentGame.current_round + 1;
        currentGame.passcount = currentGame.passcount + 1;

        stompClient.send("/app/movepass", {},
          JSON.stringify({'game_id'         : currentGameID,
                          'user_id'         : userID,
                          'current_round'   : currentGame.current_round,
                          'players_turn_id' : currentGame.players_turn_id,
                          'passcount'       : currentGame.passcount,
                          'txName'          : termID}
      ));

      updatePlayersView ();
    }
 }
/*
function movePlay ()
{
   let result = checkPlay ();

   if (result == true) {

      if (userID == currentGame.p1_iD) { currentGame.GameBoard.playMove (1); } else currentGame.GameBoard.playMove (2);
      if (currentGame.players_turn_id == currentGame.p1_iD) {
         currentGame.players_turn_id = p2_iD;
         currentGame.p1_hand = myRack.replace(/\s+/g, '');
      }
      else {
         currentGame.players_turn_id = p1_iD;
         currentGame.p2_hand = myRack.replace(/\s+/g, '');
      }
   }

   updatePlayersView
}

function sendmovePlay() {
    //connect ();

    stompClient.send("/app/moveplay", {},
                     JSON.stringify({'game_id'        : gameID,
                                     'lastplay'       : currentGame.GameBoard.getLastPlay (),
                                     'challengecount' : currentGame.challengecount,
                                     'players_turn_id': currentGame.players_turn_id,
                                     'txName':termID}
                         ));
}
*/

function hideChallengeButtons ()
{
   let myNode = document.getElementById("challenge_btn_div");
   if (myNode !== null) myNode.setAttribute ("class", "hide_challenge_btn_div");

//   myNode = document.getElementById("accept_btn");
//   if (myNode !== null) ("class", "hide_challenge_btn");
}

function showChallengeButtons ()
{
   let myNode = document.getElementById("challenge_btn_div");
//   if (myNode !== null) myNode.setAttribute ("class", "hide_challenge_btn_div");
    if (myNode !== null)
    {
       disableActionButtons ()
    }
    myNode.setAttribute ("class", "btn btn-default");

//   myNode = document.getElementById("accept_btn");
//   if (myNode !== null) myNode.setAttribute ("class", "btn btn-default");
}

function showChallengeDiv (words) {
   let myNode = document.getElementById("challenge_div");
   myNode.setAttribute ("class", "show_challenge_div");

   myNode = document.getElementById("challenge_text");

   if (currentGame.players_turn_id == userID) {
      if (currentGame.challengecount == 0) {
         myNode.innerHTML = "First Challenge";
      } else {
         myNode.innerHTML = "Second Challenge!!!!";
      }
   }

   myNode = document.getElementById("score_tbody");
   myNode.innerHTML = words;

   if (currentGame.players_turn_id == userID) {

      showChallengeButtons ();
   }
   else hideChallengeButtons ();
}

function hideChallengeDiv () {
   let myNode = document.getElementById("challenge_div");
   myNode.setAttribute ("class", "hide_challenge_div");
}

function sendwordQuery(wordList) {
    //connect ();

    stompClient.send("/app/wordquery", {},
                     JSON.stringify({'game_id'        : currentGameID,
                                     'user_id'        : userID,
                                     'wordlist'       : wordList,
                                     'txName':termID}
                         ));
}


function challenge ()
{
   let wordList = currentGame.GameBoard.retrieveListOfNewWords ();

   if (wordList != "") sendwordQuery (wordList);
}

function enableActionButtons ()
{
   $("#action_div :input").attr("disabled", false); // wow so this is JQuery
}

function disableActionButtons ()
{
   $("#action_div :input").attr("disabled", true); // wow so this is JQuery
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
        if (row['game_id'] > 0) {
           subhtml += "<tr><td><button onclick =\"sendJoinGame (" + row['game_id'] + ")\" id=\"sendJoinGame\" class=\"btn btn-default\" >Join Game " + row['game_id'] + "</button></td></tr>";
        }
    }

    myNode.innerHTML = "<h2>Available Games To Join</h2><table>" + subhtml + "</table>";
}

function movePlay  ()
{
    clearWordQueryResponse ();


    // The currentGame  object contains
    // game_id  p1_iD p2_iD p1_username p2_username p1_score p2_score letters_left current_round p1_hand p2_hand winner board players_turn_id
    // lastplay playedby challengecount passcount

    if (currentGame.GameBoard.countRetractableLetters () == 0) {
            alert ("That looks like a PASS to me. Place some letters on the board!!!!");
    }
    else
    {
        if (checkPlay () == false) {
            alert ("That move does not look good. You need to fix it!!!!");
        }
        else {
            if (currentGame.players_turn_id == currentGame.p1_iD) {
               currentGame.players_turn_id = currentGame.p2_iD;

               //currentGame.p1_hand = "";
               //for (let i = 0; i < 9; i++ ) if (myRack [i] != " ") currentGame.p1_hand += myRack [i] ;
               currentGame.p1_hand = myRack
            }
            else {
               currentGame.players_turn_id = currentGame.p1_iD;
               //currentGame.p2_hand = "";
               //for (let i = 0; i < 9; i++ ) if (myRack [i] != " ") currentGame.p2_hand += myRack [i] ;
               currentGame.p2_hand = myRack
            }

            tempLastPlay = currentGame.GameBoard.playRetractableLetters ();

            stompClient.send("/app/moveplay", {},
                    JSON.stringify({'game_id'  : currentGameID,
                                    'user_id'  : userID,
                                    'txName'   : termID,
                                    'challengecount' : currentGame.challengecount,
                                    'p1_hand'  : currentGame.p1_hand,
                                    'p2_hand'  : currentGame.p2_hand,
                                    'players_turn_id' : currentGame.players_turn_id,
                                    'lastplay' : tempLastPlay}));

            updatePlayersView ();
        }
    }
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

    myListOfGameIDs = [];

    let currentGameshtml = "<tr><th>Game Number</th><th>Opponent</th><th>Whose Turn Is It</th><th></th></tr>";

    for (let ii = 0; ii < count; ii++ ) {
        let row = objArr[ii];
        let tempOp = "";

        if (row['p1_iD'] == userID) { tempOp = row ['p2_username']; } else { tempOp = row ['p1_username'];}

        myListOfGameIDs.push (row['game_id']);

        if (row['p2_iD'] > 0) {
           // we have an active match

           let radioButton = "<input ";

           if (currentGameID == row['game_id']) radioButton +=  " checked ";

           radioButton += "onchange=\"radioChange (event) \" type=\"radio\" id=\"radio_id_" + row['game_id'] + "\" name=\"selectgame\" value=\"" + row['game_id'] + "\">";

           if (row ['players_turn_id'] == userID)
           {
              // My TURN
             currentGameshtml += "<tr><td>" + row['game_id'] + "</td><td>" + tempOp + "</td><td>My Turn</td><td>" + radioButton + "</td></tr>";
           }
           else {  currentGameshtml += "<tr><td>" + row['game_id'] + "</td><td>" + tempOp + "</td><td>Their Turn</td><td>" + radioButton + "</td></tr>"; }
        }
        else { currentGameshtml += "<tr><td>" + row['game_id'] + "</td><td>Waiting for an Opponent</td></tr>";
        }
    }

    myNode.innerHTML = "<h2>Current Games</h2><table>" + currentGameshtml + "</table>";
}


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
        myNode.innerHTML = "<table><tr class=\"player_signed_in\"><td>Player: </td><td class=\"player_name\">" + userName + "</td><td>&nbsp;&nbsp;" +
                           "<button onclick =\"logoutUser ( )\" id=\"logoutUser\" class=\"btn btn-default\" >Logout</button>" +
                           "</td><td><button onclick =\" displayUserStatus ( )\" id=\"displayUserStatus\" class=\"btn btn-default\" >Refresh</button></td></tr></table>";
        requestStatus();
    }
}

function logoutUser ()
{
   disconnect ();
   userName = "";
   userID   = 0;
   window.location.reload();
}

function radioChange (ev) {
   let target_ev = ev.target.id;
   currentGameID = ev.target.value;
   requestGameInfo (currentGameID);

}

function resetElementByID(theID) {
    document.getElementById(theID).reset();
}




