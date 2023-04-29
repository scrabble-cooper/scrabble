function checkPlay ()
{
   let result = currentGame.GameBoard.checkRetractableLetters ();

   if (result == false) { $("#clickresult").html ('<h2>Invalid Move</h2>');
   }
   else  $("#clickresult").html ('<h2>Move is Valid</h2>');

   return result;
}





function scorePlay ()
{
   let result = checkPlay ();


   if (result == true) showChallengeDiv (currentGame.GameBoard.scoreRetractableLetters ());;
}

function displayRackClickResult (htmlText) // Displays the Result of clicking a rack button e.g. score will should what the play would be worth
{
   $("#clickresult").html (htmlText);
}

function clearRackClickResult ()
{
   $("#clickresult").html ("");
}

// Also contains drag and drop javascript
function wildChooseLetter (ev)
{
   let last     = ev.target.id.lastIndexOf ("_");
   let fromType = ev.target.id.substring (0,last + 1);

   if (fromType === "board_img_")  {

      let boardOffset = ev.target.id.substr (10);

      // change the selection of the wild card letter before playing a move but while on the board
      let   tempLetter = prompt ("Please enter Wild Card Letter", "");
      if ((tempLetter != null) && (tempLetter != ""))
      {
          const aLetterRegEx = /^[a-zA-Z]{1}$/; // regex for a single alphabetic character any case

          if (aLetterRegEx.test (tempLetter)) {
              currentGame.GameBoard.placeRetractableLetter (tempLetter.toLowerCase(),boardOffset);
              currentGame.GameBoard.appendBoard ();
          }
      }
   }
}



function  doubleclickonrackletter (ev) {

   if (currentGame.GameBoard.countRetractableLetters () != 0) {
        alert ("All of your letters must be on your rack before you can start the exchange.");
   }
   else
   {
       rackOffset = ev.target.id.substr (ev.target.id.length-1);

       let tempExchange = "";
       for (let i = 0; i < 9; i++){
           if (i == rackOffset) {
              if (myExchange [i] != " ") { tempExchange += ' ';} else { tempExchange += myRack [i];}
           } else { tempExchange += myExchange [i]; }
       }
       myExchange = tempExchange;
       clearRack ();
       displayRack();
   }
}

function resetRack () {
   // Retract all retractable letters from the board
   let tempRack = "";

   for (let i = 0; i < myRack.length; i++) {
      if (myRack [i] != " ") tempRack += myRack [i]; }

   tempRack += " ";
   tempRack += currentGame.GameBoard.removeRetractableLetters ();
   while (tempRack.length < 9) tempRack += " ";

   myRack = tempRack;
   clearRack ();
   displayRack();

   currentGame.GameBoard.appendBoard ();
}


function allowDrop(ev) {
  ev.preventDefault();
}

function dragoverboard (ev) {
// may do something here if needed
}

function drag(ev) {
  //ev.dataTransfer.setData("text", ev.target.id);
  //dragFromValue =  ev.target.value;
  dragFromID    =  ev.target.id;
}

function droponboard (ev) {
  ev.preventDefault();

  myExchange = "         ";

  if (currentGame.players_turn_id != userID) {
     alert("It is not your turn!!!!");
  }
  else
  {
      let str = "From to Board Drop from " + dragFromID + " to " + ev.target.id;
      console.log (str);

      let last = dragFromID.lastIndexOf ("_");

      let fromType       = dragFromID.substring (0,last + 1);
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

             let letter = myRack[fromRackOffset];
             let wildCardUsed = false;

             if (letter === "*") {
                let   tempLetter = prompt ("Please enter Wild Card Letter", "");
                if ((tempLetter != null) && (tempLetter != ""))
                {
                    const aLetterRegEx = /^[a-zA-Z]{1}$/; // regex for a single alphabetic character any case

                    if (aLetterRegEx.test (tempLetter)) {
                       currentGame.GameBoard.placeRetractableLetter (tempLetter.toLowerCase(),toBoardOffset) // it is
                       currentGame.GameBoard.appendBoard ();
                       wildCardUsed = true;
                    }
                    else { return ; };
                }
             }
             else {
                currentGame.GameBoard.placeRetractableLetter (letter,toBoardOffset) // it is
                currentGame.GameBoard.appendBoard ();
             }

             /*
             I wanted to use substring but it always behaved weirdly when * are involved [RE TA IN*] caused issues never worked correctly when moving N

             let tempRack = myRack.substring (0,fromRackOffset);
             tempRack = tempRack + " ";
             console.log (myRack.substring (fromRackOffset+1,9))
             tempRack = tempRack + myRack.substring (fromRackOffset+1,9); //up to and excluding the last offset
             */

             let tempRack = "";
             let tempExchange = "";
             for (let i = 0; i < 9; i++){
                if (i == fromRackOffset) {
                   tempRack = tempRack + " ";
                   tempExchange += " ";

                }
                else {
                   tempRack = tempRack + myRack [i];
                   tempExchange += myExchange [i];

                }
             }

             myExchange = tempExchange;

             myRack = tempRack;
             clearRack ();
             displayRack();

          }
      } else if (fromType === "board_img_")  {
          let fromBoardOffset = dragFromID.substr (10);

          if (toType === "board_td_")
          {
             // dragging letter from rack to board
             toBoardOffset = ev.target.id.substr (9);

             currentGame.GameBoard.moveRetractableLetter (fromBoardOffset,toBoardOffset);
             currentGame.GameBoard.appendBoard ();
          }
      }
  }
  clearRackClickResult ();
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
            let tempExchange = "";
            for (let i = 0; i < 9; i++)
            {
              if (i == toRackOffset) {
                 tempRack     = tempRack + myRack [fromRackOffset];
                 tempExchange += " ";
              }
              else if (i == fromRackOffset) {
                 tempRack = tempRack + myRack [toRackOffset];
                 tempExchange += " ";
              }
              else {
                 tempRack = tempRack + myRack [i];
                 tempExchange += myExchange [i];

              }
            }

            myExchange = tempExchange;
            myRack = tempRack;
            clearRack ();
            displayRack();
         }

      }
  }

  clearRackClickResult ();
  dragFromID = "";
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
    let exchangeMarker = "";
    if (myExchange [i] != " ") { exchangeMarker = "<div class=\"rackexchangemarker\">*</div>"; }

    if (myRack[i] === " ") { // blank space in rack
        $("#rack_tr").append("<td ondragover=\"allowDrop(event)\" id=\"rack_" + i + "\" >" +
                             "<img id =\"rack_img_" + i + "\" class=\"rack-td\" ondrop=\"droponrack(event)\" src=\"images/clear.png\" /></td>");
    }
    else if (myRack[i] === "*") { // wildcard in rack - you should do well
        $("#rack_tr").append("<td ondragover=\"allowDrop(event)\"  id=\"rack_" + i + "\" ondrop=\"droponrack(event)\">" +
                             "<img id =\"rack_img_" + i + "\" class=\"rack-td\"      src=\"images/*.svg\" draggable=\"true\" ondragstart=\"drag(event)\" /></td>");
    }
    else {
        $("#rack_tr").append ("<td ondragover=\"allowDrop(event)\"  id=\"rack_" + i + "\" class=\"rack-td\"  ondrop=\"droponrack(event)\">" + exchangeMarker +
                              "<img id =\"rack_img_" + i + "\" class=\"rack-img\" src=\"images/" + myRack[i] + ".svg\" draggable=\"true\" ondragstart=\"drag(event)\"" +
                              "  ondblclick=\"doubleclickonrackletter(event)\"/></td>");
    }
}

function displayRack() {
    // should always be nine because it is padded with spaces. we want to be able to store the rack as sorted by the player
    // so even when a player has the maximum number of letters 7, there will be two spaces
    $("#rack_tr").innerHTML = '';

    for (let i = 0; i < 9; i++) displayARackLetter (i); // rack offset from 0 to 8

}





