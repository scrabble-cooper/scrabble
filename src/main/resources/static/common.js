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
  // let myNode = document.getElementById("left-new-game");
  // going to be renamed to show standings if there is time
  //myNode.innerHTML = "<button onclick =\"sendCreateGame ()\" id=\"creategame\" class=\"btn btn-default\" >New Game</button>";
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

    consoleOutLetterCount ("showResponse-begin");


    if (obj.txName == (termID))
    {
        // response is for this client it for this
       // $("#response").innerHTML = "";
        const responseNode = document.getElementById("response");
        responseNode.innerHTML = "";
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

           displayStartNewGameButton (); // Allows player to create a new game which others players can join. when they join the game starts
           displayCurrentGames (objArr,currentNumberOfGames); // show unfinished games only
           requestJoinableGames();
           displayCurrentGames (objArr,currentNumberOfGames, true); // show finished games

        } else
        if (obj.txData == "creategame")
        {
          requestStatus();
        } else
        if (obj.txData == "joingame")
        {
           clearWordQueryResponse ();
           if (obj.opponent_id == 0) {
              // someone joined the game before us :(
              currentGameID = -1;
              responseNode.innerHTML = "Too slow. Someone else joined the Game!!";

           }
           else {
             currentGameID   = obj.game_id;
             requestGameInfo (currentGameID);
           }
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
                  appendToPlayLog ("chun");

                  if (currentGame.challengecount >= 1) {
                     makeLastMovePermanent (true); // they lost the second challenge switch back to other player
                  }
                  else {
                     makeLastMovePermanent (); // they lost first challenge
                  }
               }
               else {
                   appendToPlayLog ("chok");
                   unMakeLastMove ();
               }
           }
           if (obj.txData == "requestgameinfo")
           {
              if ((currentGameID <= 0) || ((currentGameID == obj.game_id)))
              {
                 currentGame = obj;
                 myExchange  = "         ";

                 consoleOutLetterCount ("showResponse-requestgameinfo");

                 currentGameID = obj.game_id;

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

              case "moveplay":
                         requestGameInfo (currentGameID);
                         break;
              case "movepass":
                         requestGameInfo (currentGameID);
                         break;
              case "moveexchange":
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
             currentGameID = obj.game_id;
             requestGameInfo (obj.game_id);
          }


       }
       else {
          if ((obj.txData == "signin") && ( userName != "") && (obj.user_name == userName))
          {
             // The same user has signed in again so log them out this instance instantly!!!
             logoutUser ();
          }
       }
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
                       'playlog'         : currentGame.playlog,
                       'lastplay'        : currentGame.GameBoard.getLastPlay(),
                       'txName'          : termID}
      ));


}

function appendToPlayLog (theMove,arg1,arg2)     // play ltrs off , pass, exch #, noch, chok, chun, resg
{
   switch (theMove) {

      case "play":  // play, the letters played and the offset of the board for the first letter
         currentGame.playlog += currentGame.current_round + ", " + userName  + ", " + theMove  + ", " + arg1  + ", " + arg2 ;
         currentGame.playlog += ", " + currentGame.p1_score +  ", " + currentGame.p2_score + "\n";
         break;

      case "pass":  // pass
         currentGame.playlog += currentGame.current_round + ", " + userName  + ", " + theMove  +  ", " + " " +  ", " + " ";
         currentGame.playlog += ", " + currentGame.p1_score +  ", " + currentGame.p2_score + "\n";

         break;

      case "exch":  // exchanged count number of letters exchanged
         currentGame.playlog += currentGame.current_round + ", " + userName  + ", " + theMove  + ", " + arg1 + ", " + " ";
         currentGame.playlog += ", " + currentGame.p1_score +  ", " + currentGame.p2_score + "\n";
         break;

      case "noch":  // noch
         currentGame.playlog += currentGame.current_round + ", " + userName  + ", " + theMove + ", +" + currentGame.GameBoard.retrieveMovePoints ();
         currentGame.playlog += ", " + " " +  ", " + currentGame.p1_score +  ", " + currentGame.p2_score + "\n";
         break;

      case "chok":  // challenge ok succeeded
         currentGame.playlog += currentGame.current_round + ", " + userName  + ", " + theMove  +  ", " + " " +  ", " + " ";
         currentGame.playlog += ", " + currentGame.p1_score +  ", " + currentGame.p2_score + "\n";
         break;

      case "chun":  // challenged unsuccesful failed
         currentGame.playlog += currentGame.current_round + ", " + userName  + ", " + theMove  + ", +" + currentGame.GameBoard.retrieveMovePoints ();
         currentGame.playlog +=  ", " + " " +  ", " + currentGame.p1_score +  ", " + currentGame.p2_score + "\n";
         break;

      case "resg":  // gave up already failed
         currentGame.playlog += currentGame.current_round + ", " + userName  + ", " + theMove  +  ", " + " " +  ", " + " ";
         currentGame.playlog += ", " + currentGame.p1_score +  ", " + currentGame.p2_score + "\n";
         break;
   }
}

function accept ()
{
   clearWordQueryResponse (); // if any
   appendToPlayLog ("noch");
   makeLastMovePermanent ();
}

function updatePlayersView ()
{
    if (currentGameID != -1)
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

        consoleOutLetterCount ("updatePlayersView");

        if (currentGame.winner == 0)
        {
            showPlayerScores ();

            if ((currentGameID != 0) && (currentGame.GameBoard.getLastPlay () != blankStart))
            {
               let score = currentGame.GameBoard.scorePlayedLetters ();

               showChallengeDiv (currentGame.GameBoard.retrieveNewWords());
            }
            else hideChallengeDiv ();

            showNumberOfBagLetters ();

            showPlayLog (6);

            if ((currentGameID != 0) && (currentGame.GameBoard.getLastPlay () == blankStart) && (currentGame.players_turn_id == userID)) {
               enableActionButtons ();
            }
            else  disableActionButtons ();
        }
        else {
            showPlayerScores ();
            showPlayLog (200); // basically show the whole log
            hideChallengeDiv ();
            disableActionButtons ();
        }
    }
}

function makeLastMovePermanent (changeTurn = false)
{
   let player_number = 1;
   if (currentGame.players_turn_id == currentGame.p1_iD) {
      // it is player ones turn so the last move was by player 2
      player_number = 2;
   }
   consoleOutLetterCount ("MakeLastMovePermanent 11111 ");

   let lettersPlayed = currentGame.GameBoard.updateBoardWithLastPlayed (player_number);

   if (player_number == 2) {
      currentGame.p2_score += currentGame.GameBoard.retrieveMovePoints ();

      currentGame.p2_hand = currentGame.p2_hand.replaceAll(" ", '');

      consoleOutLetterCount ("MakeLastMovePermanent 22222 " + currentGame.p2_hand + " " + lettersPlayed);

      currentGame.p2_hand += dealLetters (7 - currentGame.p2_hand.length);

      // check for a winner
      // winner field -- always with respect to p1  0 : incomplete  1 : wins   2 : lost  3 : draw  4 : abandon

      if (currentGame.p2_hand.replaceAll (" ", "").length == 0) {

          // p2 finished

          let pIL = pointsInLetters (currentGame.p1_hand);
          // alter scores ie p2 gets points from other players hand
          // and they lose those points

          currentGame.p2_score += pIL;
          currentGame.p1_score -= pIL;

          if (currentGame.p1_score > currentGame.p2_score) {
             currentGame.winner = 1;
          }
          else if (currentGame.p1_score == currentGame.p2_score) {
             currentGame.winner = 3;
          } else currentGame.winner = 2;
      }

   }
   else {
      currentGame.p1_score += currentGame.GameBoard.retrieveMovePoints ();

      currentGame.p1_hand = currentGame.p1_hand.replaceAll(" ", '');

      consoleOutLetterCount ("MakeLastMovePermanent 33333 " + currentGame.p1_hand + " " + lettersPlayed);

      currentGame.p1_hand += dealLetters (7 - currentGame.p1_hand.length);

      if (currentGame.p1_hand.replaceAll (" ", "").length == 0) {

          // p1 finished

          currentGame.winner = 1;
          let pIL = pointsInLetters (currentGame.p2_hand);
          // alter scores ie p1 gets points from other players hand
          // and they lose those points

          currentGame.p1_score += pIL;
          currentGame.p2_score -= pIL;

          if (currentGame.p1_score > currentGame.p2_score) {
             currentGame.winner = 1;
          }
          else if (currentGame.p1_score == currentGame.p2_score) {
             currentGame.winner = 3;
          } else currentGame.winner = 2;
      }
    }

   if (changeTurn == true) {
      // Basically your 2nd challenge failed
      console.log ("Update Last Moved played but switch players turn id ");
      if (currentGame.players_turn_id == currentGame.p1_iD) { currentGame.players_turn_id = currentGame.p2_iD; } else { currentGame.players_turn_id = currentGame.p1_iD; }
   }

   consoleOutLetterCount ("MakeLastMovePermanent");

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
                       'winner'          : currentGame.winner,

                       'players_turn_id' : currentGame.players_turn_id,
                       'board'           : currentGame.GameBoard.getTheWords(),
                       'playedby'        : currentGame.GameBoard.getPlayedBy(),
                       'lastplay'        : currentGame.GameBoard.getLastPlay(),
                       'playlog'         : currentGame.playlog,
                       'txName'          : termID}
      ));
}

function dealLetters (dealNLetters)
{
    let result = "";
    let letterBag = currentGame.letters_left;

    console.log ("DL Start " + dealNLetters + " " + currentGame.letters_left.length);

    while ((currentGame.letters_left.length > 0) && (dealNLetters > 0))
    {
        let i = Math.floor(Math.random() * currentGame.letters_left.length);

        console.log ("DL Mid " + i);

        result += currentGame.letters_left[i];

        if (i == 0)
        {
            letterBag = currentGame.letters_left.substring (1);
        }
        else letterBag = currentGame.letters_left.substring (0,i) + currentGame.letters_left.substring (i+1);

        currentGame.letters_left = letterBag;

        dealNLetters--;
    }

    console.log ("DL ENd " + result.length + " " + currentGame.letters_left.length);

    return result;
}

function countOfNonSpaceLetters (aStr)
{
   let tempStr = aStr.replaceAll (" ", "");

   return tempStr.length;
}

function consoleOutLetterCount (markStr)
{
   if ((currentGame !== null) && (typeof currentGame.GameBoard !== "undefined"))
   {
       let tempWC = currentGame.letters_left.length + countOfNonSpaceLetters (currentGame.p1_hand) + countOfNonSpaceLetters (currentGame.p2_hand)
            + countOfNonSpaceLetters (currentGame.GameBoard.getLastPlay()) + countOfNonSpaceLetters (currentGame.GameBoard.getTheWords());

        let tempStr =  " r1 " + countOfNonSpaceLetters (currentGame.p1_hand);
            tempStr += " r2 " + countOfNonSpaceLetters (currentGame.p2_hand);
            tempStr += " lp " + countOfNonSpaceLetters (currentGame.GameBoard.getLastPlay());
            tempStr += " mb " + countOfNonSpaceLetters (currentGame.GameBoard.getTheWords());
            tempStr += " mb " + currentGame.letters_left.length;
            tempStr += " 100 == " + tempWC;
            tempStr += " pc " + currentGame.passcount;
            tempStr += " cc " + currentGame.challengecount;

       console.log (markStr + tempStr);
   }
}


function showPlayLog (nLines=5)
{
   let myNode = document.getElementById("play_log_table");

   let lines = currentGame.playlog.split('\n');

   console.log(lines[lines.length - 2]);

   let tempTable  = "";
   let tempOffset = lines.length - 2;
   let i = 0;

   while ((tempOffset >= 0) && (i < nLines))
   {
      let row =   lines[tempOffset].split (",");

      tempTable  += "<tr><td>";
      tempTable  += row [0]; // round
      tempTable  += "</td><td>";
      tempTable  += row [1]; // user
      tempTable  += "</td><td>";
      tempTable  += row [2]; // move
      tempTable  += "</td>";

      if ((row [2] == " noch") || (row [2] == " chun")) {
         tempTable  += "<td class=\"challenged_pts\">";
         tempTable  += row [3]; // points scored due to no challenge or unsuccessful challenge
         tempTable  += "</td>";
      }
      else {
         tempTable  += "<td>";
         tempTable  += row [3]; // number of letters or letters played
         tempTable  += "</td>";
      }


      if (row [4].replaceAll (" ",'') != "")
      {
      // onmouseover="highlight(this);" onmouseout="unhighlight(this)">
         tempTable += "<td><span onmouseover=\"highlight(" + row [4] + ");\" onmouseout=\"unhighlight(" + row [4] + ");\">" + row [4];
         tempTable += "</span></td>";
      }
      else {
         tempTable  += "<td>";
         tempTable  += row [4];
         tempTable  += "</td>";
      }
      tempTable  += "<td>";
      tempTable  += row [5]; // p1_score
      tempTable  += "</td>";
      tempTable  += "<td>";
      tempTable  += row [6]; // p2_score
      tempTable  += "</td>";


      tempOffset--;
      i++;
      tempTable  += "</tr>";

   }
   myNode.innerHTML =  tempTable;
}

var savedStyle ;

function highlight(value)
{
   let element = document.getElementById('board_td_' + value);

   if (window.getComputedStyle) {
       savedStyle = window.getComputedStyle(element);
   } else {
       savedStyle = element.currentStyle;
   }

   element.style.backgroundColor = "orange";
   element.style.border = "thick dashed orange";
   console.log(value);
}

function unhighlight(value)
{
   let element = document.getElementById('board_td_' + value);
   element.style = savedStyle

   console.log(value);
}

$(".highLightMe").mouseover(function () {
       console.log($(this).val());
});


function showNumberOfBagLetters ()
{
   let myNode = document.getElementById("letters_div");

   //DO NOT REMOVE VERY USEFUL FOR DEBUGGING WEIRD HAPPENINGS
   //debug remove before publishing.
   /*
   let tempWC = currentGame.letters_left.length + countOfNonSpaceLetters (currentGame.p1_hand) + countOfNonSpaceLetters (currentGame.p2_hand)
       + countOfNonSpaceLetters (currentGame.GameBoard.getLastPlay()) + countOfNonSpaceLetters (currentGame.GameBoard.getTheWords());

   let tempStr =  " r1 " + countOfNonSpaceLetters (currentGame.p1_hand);
       tempStr += " r2 " + countOfNonSpaceLetters (currentGame.p2_hand);
       tempStr += " lp " + countOfNonSpaceLetters (currentGame.GameBoard.getLastPlay());
       tempStr += " mb " + countOfNonSpaceLetters (currentGame.GameBoard.getTheWords());
       tempStr += " 100 == " + tempWC;
       tempStr += " pc " + currentGame.passcount;
       tempStr += " cc " + currentGame.challengecount;
   */
   let passStr = "";

   if (currentGame.passcount == 1) passStr = "First Pass. Three consecutive passes and the game is a draw.";

   if (currentGame.passcount == 2) passStr = "Second Pass. Three consecutive passes and the game is a draw.";

   myNode.innerHTML =  "<span title=\"Number of letters left to be dealt.\">" + currentGame.letters_left.length + " <meter  id=\"letters_dealt\" value=\"" + currentGame.letters_left.length +
                       "\" min=\"0\" max=\"100\"></meter></span><br>" + passStr;


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

   myNode = document.getElementById("winner_loser_div");

   switch(currentGame.winner) {
      case 1:
                myNode.innerHTML = "GAME OVER " + currentGame.p1_username  + " is the WINNER";
                break;
      case 2:
                myNode.innerHTML = "GAME OVER " + currentGame.p2_username  + " is the WINNER";
                break;
      case 3:
                myNode.innerHTML = "GAME OVER. Its a DRAW";
                break;
      default:
                myNode.innerHTML = "";
   }

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

      consoleOutLetterCount ("MoveExchange");

      currentGame.letters_left += forTheBag ;

      appendToPlayLog ("exch",(7 - tempHand.length));

      currentGame.current_round++;

      stompClient.send("/app/moveexchange", {},
           JSON.stringify({'game_id'         : currentGameID,
                           'user_id'         : userID,
                           'current_round'   : currentGame.current_round,
                           'p1_hand'         : currentGame.p1_hand,
                           'p2_hand'         : currentGame.p2_hand,
                           'letters_left'    : currentGame.letters_left,
                           'players_turn_id' : currentGame.players_turn_id,
                           'playlog'         : currentGame.playlog,
                           'txName'          : termID}
       ));

       updatePlayersView ();



   }
}

function moveResign ()
{
    clearWordQueryResponse ();

    let   tempReply = prompt ("RESIGN!!! Are you really sure? What would your mother think? type Y to confirm.", "NO");

    if ((tempReply.toUpperCase () == "Y") || (tempReply.toUpperCase () == "YES"))
    {
        resetRack ();

        if (currentGame.players_turn_id == currentGame.p1_iD) {
           currentGame.winner = 2;
        }
        else currentGame.winner = 1;

        appendToPlayLog ('resg');

        stompClient.send("/app/movepass", {},
          JSON.stringify({'game_id'         : currentGameID,
                          'user_id'         : userID,
                          'current_round'   : currentGame.current_round,
                          'players_turn_id' : currentGame.players_turn_id,
                          'winner'          : currentGame.winner,
                          'passcount'       : currentGame.passcount,
                          'playlog'         : currentGame.playlog,
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


        appendToPlayLog ("pass");


        currentGame.current_round = currentGame.current_round + 1;
        currentGame.passcount = currentGame.passcount + 1;

        if (currentGame.passcount == 3) currentGame.winner = 3; // Three passes in a row and it is a draw.



        stompClient.send("/app/movepass", {},
          JSON.stringify({'game_id'         : currentGameID,
                          'user_id'         : userID,
                          'current_round'   : currentGame.current_round,
                          'players_turn_id' : currentGame.players_turn_id,
                          'winner'          : currentGame.winner,
                          'passcount'       : currentGame.passcount,
                          'playlog'         : currentGame.playlog,
                          'txName'          : termID}
      ));

      updatePlayersView ();
    }
 }

function hideChallengeButtons ()
{
   $("#challenge_btn_div :button").attr("disabled", true); // wow so this is JQuery
}


function showChallengeButtons ()
{
   $("#challenge_btn_div :button").attr("disabled", false); // wow so this is JQuery
   disableActionButtons ();
}

function showChallengeDiv (words) {

   let myNode = document.getElementById("challenge_div");
   myNode.setAttribute ("class", "show_challenge_div");

   myNode = document.getElementById("score_tbody");
   myNode.innerHTML = words;

   myNode = document.getElementById("challenge_text");
   if (currentGame.challengecount == 0) {
       myNode.innerHTML = "<span class=\"first_chall\">&nbsp;First Challenge</span>";
   } else myNode.innerHTML = "<span class=\"second_chall\">&nbsp;SECOND CHALLENGE</span>";

   if (currentGame.players_turn_id == userID) {
      showChallengeButtons ();
   }
   else {
      hideChallengeButtons ();
   }
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

   myNode.innerHTML = "<span class=\"nogames\">No Joinable Games</span>";
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
           subhtml += "<tr><td>" + row['game_id'] + "</td>";
           subhtml += "<td>" + row['opponent_name'] + "</td>";
           subhtml += "<td><button onclick =\"sendJoinGame (" + row['game_id'] + ")\" id=\"sendJoinGame\" class=\"btn btn-default\" >Join</button></td></tr>";
        }
    }

    myNode.innerHTML = "<table><tr><th>Game</th><th>Opponent</th><td></th></tr>" + subhtml + "</table>";
}

function movePlay  ()
{
    consoleOutLetterCount ("moveplay-begin");

    clearWordQueryResponse ();

    consoleOutLetterCount ("moveplay-begin");

    // The currentGame  object contains
    // game_id  p1_iD p2_iD p1_username p2_username p1_score p2_score letters_left current_round p1_hand p2_hand winner board players_turn_id
    // lastplay playedby challengecount passcount

    if (currentGame.GameBoard.countRetractableLetters () == 0) {
            alert ("That looks like a PASS to me. Place some letters on the board!!!!");
    }
    else
    {
       consoleOutLetterCount ("moveplay-2");

        if (checkPlay () == false) {
            alert ("That move does not look good. You need to fix it!!!!");
        }
        else {
            if (currentGame.players_turn_id == currentGame.p1_iD) {
               currentGame.players_turn_id = currentGame.p2_iD;

               //currentGame.p1_hand = "";
               //for (let i = 0; i < 9; i++ ) if (myRack [i] != " ") currentGame.p1_hand += myRack [i] ;
               currentGame.p1_hand = myRack
               consoleOutLetterCount ("moveplay-3");
            }
            else {
               currentGame.players_turn_id = currentGame.p1_iD;
               //currentGame.p2_hand = "";
               //for (let i = 0; i < 9; i++ ) if (myRack [i] != " ") currentGame.p2_hand += myRack [i] ;
               currentGame.p2_hand = myRack
               consoleOutLetterCount ("moveplay-4");
            }

            consoleOutLetterCount ("moveplay-5");

            tempLastPlay = currentGame.GameBoard.playRetractableLetters ();

            let offset = tempLastPlay.search(/\S/);

            appendToPlayLog ("play",tempLastPlay.replaceAll (" ", ''), offset);

            stompClient.send("/app/moveplay", {},
                    JSON.stringify({'game_id'  : currentGameID,
                                    'user_id'  : userID,
                                    'txName'   : termID,
                                    'challengecount' : currentGame.challengecount,
                                    'p1_hand'  : currentGame.p1_hand,
                                    'p2_hand'  : currentGame.p2_hand,
                                    'players_turn_id' : currentGame.players_turn_id,
                                    'playlog'  : currentGame.playlog,
                                    'lastplay' : tempLastPlay}));

            updatePlayersView ();
        }
        consoleOutLetterCount ("moveplay-end");

    }
}

function setAndShowCurrentGame (gameId)
{
   if (gameId > 0){
      currentGameID = gameId;
      requestGameInfo (currentGameID);
   }
}

function displayCurrentGames (objArr,count,finished = false) // only show finished or current games
{
    let myNode = document.getElementById("left-current-games");

    if (finished == true) myNode = document.getElementById("left-old-games");

    myListOfGameIDs = [];

    let countofRowstoDisplay = 0;

    let currentGameshtml = "<tr><th>Current</th><th>Opponent</th><th>Turn</th><th></th></tr>";

    if (finished == true)
    {
       currentGameshtml = "<tr><th>Ended</th><th>Opponent</th><th><span class=\"player_name\">" + userName + "</span></th><th></th></tr>";
    }

    //currentGameshtml = "<tr><th>Current</th><th>Opponent</th><th>Turn</th><th></th></tr>";

    for (let ii = 0; ii < count; ii++ ) {
        let row = objArr[ii];
        let tempOp = "";

        if (row['p1_iD'] == userID) { tempOp = row ['p2_username']; } else { tempOp = row ['p1_username'];}

        if (row ['winner'] == 0) myListOfGameIDs.push (row['game_id']);

        if (row['p2_iD'] > 0) {
           // we have an active match may have finished

           let radioButton = "<input ";

           if (currentGameID == row['game_id']) radioButton +=  " checked ";

           radioButton += "onchange=\"radioChange (event) \" type=\"radio\" id=\"radio_id_" + row['game_id'] + "\" name=\"selectgame\" value=\"" + row['game_id'] + "\">";

           if ((finished == false) && (row ['winner'] == 0)) {
               if (row ['players_turn_id'] == userID)
               {
                  // My TURN
                 currentGameshtml += "<tr><td>" + row['game_id'] + "</td><td>" + tempOp + "</td><td>Mine</td><td>" + radioButton + "</td></tr>";
               }
               else {  currentGameshtml += "<tr><td>" + row['game_id'] + "</td><td>" + tempOp + "</td><td>Theirs</td><td>" + radioButton + "</td></tr>"; }
               countofRowstoDisplay++;

           }
           else if ((finished == true) && (row ['winner'] >= 1))
           {
              currentGameshtml += "<tr><td>" + row['game_id'] + "</td><td>" + tempOp + "</td><td>";

              if ( row ['winner'] == 3) { currentGameshtml += "Draw"; }
              {
                  if (userID == row ['p1_iD'] ) {
                      if ( row ['winner'] == 1)  { currentGameshtml += "Won"; } else  if ( row ['winner'] == 2)  { currentGameshtml += "Lost"; }
                  }
                  else  if ( row ['winner'] == 2)  { currentGameshtml += "Won"; } else  if ( row ['winner'] == 1)  { currentGameshtml += "Lost"; }
              }

              currentGameshtml += "</td><td>" + radioButton + "</td></tr>";
              countofRowstoDisplay++;
           }
        }
        else if (finished == false) {
            countofRowstoDisplay++;
            currentGameshtml += "<tr><td>" + row['game_id'] + "</td><td>waiting</td></tr>";
        }
    }

    if (countofRowstoDisplay == 0) {
        if (finished == false) { myNode.innerHTML = "<span class=\"nogames\">No Current Games</span>"; }
        else { myNode.innerHTML = "<span class=\"nogames\">No Games Finished</span>";
}
    }
    else myNode.innerHTML = "<table>" + currentGameshtml + "</table>";
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
        myNode.innerHTML = "<table><tr class=\"player_signed_in\"><td></td><td class=\"player_name\">" + userName + "&nbsp;&nbsp;</td>"
                        + "<td><button onclick =\"sendCreateGame ()\" id=\"creategame\" class=\"btn btn-default\" >New</button></td>"
                        + "<td><button onclick =\"logoutUser ( )\" id=\"logoutUser\" class=\"btn btn-default\" >Logout</button></td>" +
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
   clearWordQueryResponse ();
   requestGameInfo (currentGameID);
}

function resetElementByID(theID) {
    document.getElementById(theID).reset();
}




