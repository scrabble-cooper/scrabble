
                      // remember lowercase letters are due to the wild card and are valued 0 points
var letterValues =  {  'A':1, 'B':3, 'C':3, 'D':2,  'E':1, 'F':4, 'G':2, 'H':4, 'I':1, 'J':8, 'K':5, 'L':1, 'M':3,
                       'N':1, 'O':1, 'P':3, 'Q':10, 'R':1, 'S':1, 'T':1, 'U':1, 'V':4, 'W':4, 'X':8, 'Y':4, 'Z':10,
                       'a':0, 'b':0, 'c':0, 'd':0,  'e':0, 'f':0, 'g':0, 'h':0, 'i':0, 'j':0, 'k':0, 'l':0, 'm':0,
                       'n':0, 'o':0, 'p':0, 'q':0,  'r':0, 's':0, 't':0, 'u':0, 'v':0, 'w':0, 'x':0, 'y':0, 'z':0 };

var previousStem;

const scrabbleBk = "TBB2BBBTBBB2BBT" +
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

const midSquare  = 112; // this is the offset (starting from 0) of the mid square which must be covered by the first word played

const blankStart =
                   "               " +
                   "               " +
                   "               " +
                   "               " +
                   "               " +
                   "               " +
                   "               " +
                   "               " +
                   "               " +
                   "               " +
                   "               " +
                   "               " +
                   "               " +
                   "               " +
                   "               " ;

function  showLetters (theLetters) {
  // a debug function not in the class as it is useful elsewhere

  let str = "";
  for (let i = 0; i < theLetters.length; i++)
  {
     if (((i%75) == 0) && (i>0)) str += "\n"; // wraps every 75 characters
     str += (i%10);
  }
  console.log (str);
  str = "";
  for (let i = 0; i < theLetters.length; i++)
  {
     if (((i%75) == 0) && (i>0)) str += "\n";
     if (theLetters [i] != " ") { str += theLetters [i]; } else { str += "_"; }
  }
  console.log (str);

}

function pointsInLetters (pointMe)  // nothing to do with the board class but put here because of the letterValues array above
{
  let tempScore = 0;

  for (let i = 0; i < pointMe.length; i++) {
     if ((pointMe[i] != " ") && (pointMe[i] != "*")) tempScore += letterValues [pointMe [i]];
  }

  return tempScore;
}

class board {
   // class assumes the element ID passed is a <table> and that this class controls all the rows and columns
   #node;
   #width;
   #length;
   #background;
   #thewords;
   #retractable;
   #lastplay;
   #playedby;
   #newwords;               // Populates when a move is scored. It lists the new words with their individual scores and multipliers. plus any bingo bonus
                            // "COOPER", word score <point value including letter multipliers>, word multipliers, total row value
                            // also "Bingo", 50, ,50 for bingoes

    /* background of table square
       T = Triple Word Score
       D = Double Word Score
       B = Blank - no extra scores
       2 = Double Letter Score
       3 = Triple Letter Score
    */

   constructor (nodeC,widthC, lengthC, backC, wordsC="", playedbyC="", lastplayC="") {

      this.#node        = nodeC;
      this.#width       = widthC;
      this.#length      = lengthC;
      this.#background  = backC;
      this.#thewords    = wordsC;        // not draggable nor droponable ( when scrabble going to allow that word ) !!
      this.#retractable = blankStart;
                                         // letters that maybe be dragged back to the rack or moved around on the board
      this.#lastplay    = lastplayC;     // the last move. can be challenged but are not draggable nor droponable
      this.#playedby    = playedbyC;     // numeric 1 to 2 indicating who played the letter and allows an indication to be displayed eg different background color
                                         // eg player one tiles could be surrounded by blue background while player two green etc
      this.#newwords    = [];            // Populates when a move is scored. It lists the new words with their individual scores and multipliers. plus any bingo bonus
   }

   playMove (player) // player must be 1 or 2
   {
      //let tempLastPlay = "";
      let tempLastPlay = "";
      let tempPlayedBy = "";

      for (let i=0; i < this.#length; i++)
      {
         if (this.#retractable [i] != " ") {
            tempLastPlay += this.#retractable [i];
            tempPlayedBy += player;
         }
         else
         {
            tempLastPlay += this.#lastplay [i];
            tempPlayedBy += this.#playedby [i];
         }
      }
      this.#lastplay = tempLastPlay;
      this.#playedby = tempPlayedBy;
      this.#retractable = blankStart;
   }

   placeRetractableLetter (letter,offset) // it is always draggable
   {
      let temp = "";
      for (let i = 0; i < this.#length; i++) {
         if (i != offset) { temp += this.#retractable [i]; } else { temp += letter; }
      }
      this.#retractable = temp;

      //tempNode.innerhtml = this.createLetterImageNode (letter,offset,true);

   }

   moveRetractableLetter (fromBoardOffset,toBoardOffset)
   {
      let tempLetter = this.#retractable [fromBoardOffset];
      let temp = "";

      for (let i = 0; i < this.#length; i++) {
         if (i == fromBoardOffset) { temp += " "; }
         else if (i == toBoardOffset) { temp += tempLetter; }
         else temp += this.#retractable [i];
      }
      this.#retractable = temp;
   }

   removeRetractableLetters ()
   {
      // remove retractable letter from the board. they are going back to the rack
      let result = "";
      let temp   = "";

      for (let i = 0; i < this.#length; i++) {
         if (this.#retractable [i] != " ") {
            if ((this.#retractable.charCodeAt(i) >= 65) && (this.#retractable.charCodeAt(i) <= 90)) {
               // upper case
               result += this.#retractable [i];
            }
            else { result += "*"; }
         }
         temp += " ";
      }
      this.#retractable = temp;
      return result;
   }


   createLetterImageNode (letter,i,draggable)
   {
      let result = document.createElement ("img");

      if ((letter.charCodeAt() >= 65) && (letter.charCodeAt() <= 90)) { // check for upper case
            result.setAttribute ("src", "images/" + letter + ".svg");
            result.setAttribute ('class', "board-image"); // should be smaller would be nice to create another 26 letters svgs with zero points if we have time
      }
      else {
         result.setAttribute ("src", "images/" + letter + ".svg");
         result.setAttribute ('class', "board-wild-img");
         result.addEventListener ("dblclick", wildChooseLetter );
      }

      result.setAttribute ('id', "board_img_" + i);

      if (draggable == true) {
          result.setAttribute     ('draggable', true);
          result.addEventListener ("drag",    drag );
      }
      else {result.setAttribute   ('draggable', false) };

      return result;
   }

   createTRNode (nodeID,nodeClass)
   {
      let result = document.createElement ("tr");
      if (nodeClass != "") result.setAttribute ('class', nodeClass);
      if (nodeID != "")    result.setAttribute ('id',nodeID);

      return result;
   }

   countRetractableLetters ()
   {
      let result = 0;

      for (let i = 0; i < this.#retractable.length; i++) if (this.#retractable [i] != " ") result++;

      return result;
   }

   createTDNode (nodeID,i)
   {
      let result = document.createElement("td");
      if (nodeID != "")    result.setAttribute ('id',nodeID + i);

      if  (this.#thewords [i] !== " ") {
          switch(this.#playedby[i]) {
             case "1":
                        result.setAttribute ('class', "player-one");
                        break;
             case "2":
                        result.setAttribute ('class', "player-two");
                        break;
             default:
                        result.setAttribute ('class', "player-unknown");
          }

          result.appendChild ( this.createLetterImageNode (this.#thewords[i],i,false));
      }
      else if (this.#lastplay [i] !== " ") {
          result.setAttribute ('class', "last-play");
          result.appendChild (this.createLetterImageNode (this.#lastplay[i],i,false));
      }
      else if (this.#retractable [i] !== " ") {
          result.setAttribute ('class', "retractable");
          result.appendChild (this.createLetterImageNode (this.#retractable[i],i,true));
      }
      else
      {
          switch(this.#background[i]) {
             case "T":
                        result.setAttribute ('class', "triple-word");
                        result.innerHTML = "Triple Word";
                        break;
             case "D":
                        result.setAttribute ('class', "double-word");
                        result.innerHTML = "Double Word";
                        break;
             case "3":
                        result.setAttribute ('class', "triple-letter");
                        result.innerHTML = "Triple Letter";
                        break;
             case "2":
                        result.setAttribute ('class', "double-letter");
                        result.innerHTML = "Double Letter";
                        break;
             default:
                        result.setAttribute ('class', "plain-score");
                        result.innerHTML = "";
          }

          result.addEventListener("dragover", allowDrop );
          result.addEventListener("drop",     droponboard );
      }
      return result;
   }

   cFO (offset) // return the Column From Offset
   {
      return offset % this.#width;
   }

   rFO (offset) // return the Row From Offset
   {
      return ~~(offset / this.#width) ;
   }

   adLA (offset) // return blank or an existing letter from Above
   {
      let currentColumn = this.cFO (offset);
      let result = ' ';
      let i = offset - this.#width;
      if ((i >= 0) && (this.cFO (i) == currentColumn)) result = this.#thewords [i];
      return result;
   }

   adLB (offset) // return blank or an existing letter from Below
   {
      let currentColumn = this.cFO (offset);
      let result = ' ';
      let i = offset + this.#width;
      if ((i < this.#length) && (this.cFO (i) == currentColumn)) result = this.#thewords [i];
      return result;
   }

   adLL (offset) // return blank or an existing letter to the Left
   {
      let currentRow = this.rFO (offset);
      let result = ' ';
      let i = offset - 1;
      if ((i >= 0) && (currentRow == this.rFO (i))) result = this.#thewords [i];
      return result;
   }
   adLR (offset) // return blank or an existing letter to the Right
   {
      let currentRow = this.rFO (offset);
      let result = ' ';
      let i = offset + 1;
      if ((i >= 0)  && (currentRow == this.rFO (i))) result = this.#thewords [i];
      return result;
   }

   adjacentLetter (offset) // return true if this offset has a letter above below or either side
   {
      let result = false;
      if (this.adLA (offset) == ' ') {
          if (this.adLB (offset) == ' ') {
              if (this.adLL (offset) == ' ') {
                  if (this.adLR (offset) != ' ') result = true;
              }
              else result = true;
          }
          else result = true;
      }
      else result = true;
      return result;
   }

   anyMTSpacesBetween (start,second, direction) // either horizontally or vertically are the any empty spaces between the first and second offsets
   {
      let result = false;

      if (direction == "V")
      {
         start += this.#width;
         while (start < second) {
            if (this.#thewords [start] == " ")
            {
               // there is a gap in the letters not filled by existing letters
               result = true;
               break;
            }
            else start += this.#width;
         }
      }
      else
      {
         start += 1;
         while (start < second) {
            if (this.#thewords [start] == " ")
            {
               // there is a gap in the letters not filled by existing letters
               result = true;
               break;
            }
            else start += 1;
         }
      }

      return result;
   }

   findNextRetractableOffset (fromOffset)
   {
      let result = -1;
      for (let i = fromOffset; i < this.#length; i++) {
         if (this.#retractable [i] !=  " ")
         {
            result = i;
            break;
         }
      }
      return result;
   }

   scoreRetractableLetters ()
   {
      return this.scoreLetters (this.#retractable);
   }

   scorePlayedLetters ()
   {
      return this.scoreLetters (this.#lastplay);
   }

   scoreLetters (theseLetters)
   {
      this.#newwords = [];

      showLetters (theseLetters); // debug to console
      // Score from the first letter offset vertically and then horizontally

      // find the first offset

      let firstLetterOffset = -1;

      for (let i = 0; i < this.#length; i++) {
         if (theseLetters [i] !=  " ") {
            firstLetterOffset = i;
            break;
         }
      }

      if (firstLetterOffset != -1)
      {
          let score = 0;

          score =  this.scoreOffsetOn (firstLetterOffset,theseLetters,this.#width); //vertically
          score += this.scoreOffsetOn (firstLetterOffset,theseLetters, 1); //horizontally

          // need to check for bingoes (bingoes is in the scrabble dictionary) where seven letters have been played an add to score played

          let count = 0;
          for (let j = 0; j < this.#length; j++) if (theseLetters [j] != " ") count++;

          if (count == 7) {
             console.log ("Score " + score + " Bingo Played +50 bonus [" + (score+50) + "]");
             this.#newwords.push ({word:"",ts:0,multi:0,sc:50,comment:"Bonus for using seven letters."});
             score = score + 50;
          }


          this.#newwords.push ({word:"",ts:0,multi:0,sc:score});

          return score;
      }
      this.#newwords.push ({word:"",ts:0,multi:0,sc:0});

      return "No Last Move";
   }

   scorePreviouslyPlayedLetters (firstLetterOffset,decrement) // decrement is one for Horizontal and this.#width for vertical
   {
       let column = this.cFO (firstLetterOffset);
       let row    = this.rFO (firstLetterOffset);

       let score = 0;
       previousStem = ""; // we will need this when we show word with scores

       let offset = firstLetterOffset - decrement;


       while ((offset >= 0) && (this.#thewords[offset] != " ") &&
               (( (decrement == this.#width) && (column == this.cFO (offset) ) ) // ensure the loop stays in the same row or column
                  ||                                                             //
               ( (decrement == 1)            &&  (row    == this.rFO (offset))) ) // as the firstLetterOffset
       )
       {
          score += letterValues [this.#thewords[offset]];
          previousStem = this.#thewords[offset] + previousStem;
          offset = offset - decrement;
       }

       return score;
   }

   scorePerpendicular (offset,theseLetters,step)
   {
      // prefix and suffix to current offset.
      let prefix = '';
      let suffix = '';

      let tempScore = 0;

      let i = offset;

      let column = this.cFO (offset);
      let row    = this.rFO (offset);

      if (step == 1)
      {
         i = offset - this.#width;

         while ((i >= 0) && (column == this.cFO (i)) && (this.#thewords [i] != " "))
         {
            prefix = this.#thewords [i] + prefix;
            tempScore += letterValues [this.#thewords [i]];
            i -= this.#width;
         }

         i = offset + this.#width;

         while ((i < this.#length) && (column == this.cFO (i)) && (this.#thewords [i] != " "))
         {
            suffix = suffix + this.#thewords [i] ;
            tempScore += letterValues [this.#thewords [i]];
            i += this.#width;
         }

      }
      else
      {
         i = offset - 1;

         while ((i >= 0) && (row == this.rFO (i)) && (this.#thewords [i] != " "))
         {
            prefix = this.#thewords [i] + prefix;
            tempScore += letterValues [this.#thewords [i]];
            i -= 1;
         }

         i = offset + 1;

         while ((i < this.#length) && (row == this.rFO (i)) && (this.#thewords [i] != " "))
         {
            suffix = suffix + this.#thewords [i];
            tempScore += letterValues [this.#thewords [i]];
            i += 1;
         }

      }

      let wordMultiplier = 1;

      switch(this.#background [offset]) {
         case "T":
            wordMultiplier = wordMultiplier * 3;
            tempScore  += letterValues [theseLetters [offset]];
            break;
         case "D":
            wordMultiplier = wordMultiplier * 2;
            tempScore  += letterValues [theseLetters [offset]];
            break;
         case "3":
            tempScore  += 3 * (letterValues [theseLetters [offset]]);
            break;
         case "2":
            tempScore  += 2 * (letterValues [theseLetters [offset]]);
            break;
         default:
            tempScore  += letterValues [theseLetters [offset]];
      }

      let score = tempScore * wordMultiplier;

      let tempWord = prefix + theseLetters [offset] + suffix;

      if (tempWord.length > 1)
      {
         console.log ("Perpendicular Word: " + tempWord + " word score " + tempScore + " multiplier " + wordMultiplier + " = [" + score + "]");

         this.#newwords.push ({word:tempWord,ts:tempScore,multi:wordMultiplier,sc:score});

      }
      else score = 0;

      return score;
   }

   retrieveNewWords (format = "tablerows")
   {
      let i = 0;
      let result = "";

      if ((format == "tablerows") && (this.#newwords.length > 1)) {
         //result  = "<tr><th>Played</th><th>&nbsp;&nbsp;</th><th>&nbsp;&nbsp;</th><th>Score</th></tr>";
      }

      while (i < this.#newwords.length) {
          let row = this.#newwords [i];
          if (format == "tablerows") {
             result += "<tr class=\"scored-line\"><td class=\"played_word\">" + row ['word'] + "</td><td class=\"played_points\" >";
             if (row ['ts'] > 0 ) result += row ['ts'];
             result += "</td><td class=\"played_multiplier\">";
             if (row ['multi'] > 0 ) result += " x" + row ['multi'];
             result += "</td><td class=\"played_score\">" + row ['sc'] + "</td>";
             if (typeof row ['comment'] !== 'undefined' ) result +=  "<td class=\"played_comment\">" + row ['comment'] + "</td>";
             result += "</tr>";
          }
          else { result = row ['word'] + ", " + row ['ts'] + ", " + row ['multi'] + ", " + row ['sc'] + "\n"; }
          i++;
      }
      return result;
   }

   retrieveListOfNewWords ()
   {
      let i = 0;
      let result = "";

      while (i < this.#newwords.length) {
          let row = this.#newwords [i];
          if (row ['word'] != "") {
             if (result != "" )  { result += " __ " + row ['word']; } // going to be altered in sql to [OR word = "]
             else {
                result += row ['word'];
             }
          }
          i++;
      }

      return result;
   }

   retrieveMovePoints ()
   {
      let i   = this.#newwords.length - 1;
      let row = this.#newwords [i];

      return row ['sc'];
   }

   scoreOffsetOn  (firstLetterOffset,theseLetters,step)
   {
      // score any letters previously played before this offset
      // then add this offset ONCE as it will processed both vertically and horizontally
      // then process any letters that are following this offset in theseLetters or previously played
      // also process any "new words" that are perpendicular to this direction but not the first offset

      let column = this.cFO (firstLetterOffset);
      let row    = this.rFO (firstLetterOffset);

      let tempScore = this.scorePreviouslyPlayedLetters (firstLetterOffset,step);

      let tempWord   = previousStem;

      let tempOffset    = firstLetterOffset;
      let tempLetter    = theseLetters[firstLetterOffset];
      let isTheseLetter = true; // it is from theseLetters
      let wordMultiplier = 1;

      let perpendicularScore = 0;

      while (tempLetter != " ") {
         tempWord    = tempWord + tempLetter;

         if ( isTheseLetter == true) {

            if (tempOffset != firstLetterOffset) perpendicularScore += this.scorePerpendicular (tempOffset,theseLetters,step);

            switch(this.#background [tempOffset]) {
              case "T":
                wordMultiplier = wordMultiplier * 3;
                tempScore  += letterValues [tempLetter];
                break;
              case "D":
                wordMultiplier = wordMultiplier * 2;
                tempScore  += letterValues [tempLetter];
                break;
              case "3":
                tempScore  += 3 * (letterValues [tempLetter]);
                break;
              case "2":
                tempScore  += 2 * (letterValues [tempLetter]);
                break;
              default:
                tempScore  += letterValues [tempLetter];
            }
         }
         else tempScore  += letterValues [tempLetter];

         tempOffset = tempOffset + step;

         if ((tempOffset < this.#length) && ( ( (step == 1) && (row == this.rFO (tempOffset) ) ) || ( (step == this.#width) && (column == this.cFO (tempOffset) ) ) ) )
         {
             tempLetter            = theseLetters[tempOffset];;
             if (tempLetter != " ") {
                isTheseLetter = true;
             }
             else {
                isTheseLetter = false;
                tempLetter = this.#thewords [tempOffset];
             }
         }
         else tempLetter = " ";
      }

      let score = wordMultiplier * tempScore;

      if (tempWord.length > 1)
      {
         this.#newwords.push ({word:tempWord,ts:tempScore,multi:wordMultiplier,sc:score});
         console.log ("Main Word: " + tempWord + " word score " + tempScore + " multiplier " + wordMultiplier + " = [" + score + "]" + " Perpendicular Score " + perpendicularScore);
      }
      else
      {
         tempScore = 0;
         score = 0;
         perpendicularScore = 0;
      }

      return score + perpendicularScore;
   }

   checkRetractableLetters () // tests to see if Retractable letters are placed correctly
                              // 1) all in a single row or single column
                              // 2) gaps between the letters are filled by letters from existing words
                              // 3) if this is the very first word played, the word goes through the centre starting square
                              // 4) if this is not the first word played at least one letter is adjacent to an existing word
   {
      let result = false;

      showLetters (this.#retractable);
      // Find the first letter

      let firstLetterOffset = -1;
      let firstColumn       = -1;
      let firstRow          = -1;
      let adjacentFound     = false;

      firstLetterOffset = this.findNextRetractableOffset (0);

      if (firstLetterOffset != -1)
      {
         firstColumn = this.cFO (firstLetterOffset);
         firstRow    = this.rFO (firstLetterOffset);

         adjacentFound = this.adjacentLetter (firstLetterOffset);

         let secondLetterOffset = -1;
         let secondColumn       = -1;
         let secondRow          = -1;

         // now find the second retractable letter - if any
         secondLetterOffset = this.findNextRetractableOffset (firstLetterOffset + 1);

         if (secondLetterOffset == -1) {
            // only one letter played move is only valid if played next to an existing word

            result = adjacentFound;

         } else {
            let playDirection = "";
            // was the play vertical or horizontal

            if (firstColumn == this.cFO (secondLetterOffset)) {
               playDirection = "V"; //for vertical

            } else if (firstRow == this.rFO (secondLetterOffset)) {
               playDirection = "H"; // horizontal
            }
            else {
               console.log ("No direction Result " + result );

               return result ; // e just return with false as the first two retractable letters are not in a straight line
            }

// #################### there is a return above just in case you missed it #####################################################

            result = !this.anyMTSpacesBetween (firstLetterOffset,secondLetterOffset, playDirection);

            if (!adjacentFound) adjacentFound = this.adjacentLetter (secondLetterOffset);

            let prevOffset = secondLetterOffset ;
            while (result)
            {
               let nextOffset = this.findNextRetractableOffset (prevOffset + 1);

               if (nextOffset == -1) break;
               if ((playDirection == "V")  && (firstColumn != this.cFO (nextOffset))) { result = false; }
               else {
                   if ((playDirection == "H")  && (firstRow    != this.rFO (nextOffset))) { result = false; }
                   else {
                      result = !this.anyMTSpacesBetween (prevOffset ,nextOffset, playDirection);

                      if (!adjacentFound) adjacentFound = this.adjacentLetter (nextOffset);

                      prevOffset = nextOffset;
                   }
               }
            }

            console.log ("Pre Check Result " + result + " " + adjacentFound + " [" + this.#retractable [112] + "]" );
            if (result == true) {
                 // word is in straight line with no gaps but it is not adjacent to a previous word nor is it through the middle spot as a first play
                 if ((adjacentFound == false) && this.#retractable [112] === " ") result = false;
            }
         }
      }

      console.log ("Check Result " + result);
      return result;
   }

   getLastPlay () {
      return this.#lastplay;
   }

   getTheWords () {
      return this.#thewords;
   }

   getPlayedBy () {
      return this.#playedby;
   }

   playRetractableLetters ()
   {
      this.#lastplay  = this.#retractable;
      this.#retractable = blankStart;
      return this.#lastplay;
   }

   clearAndReturnLastPlayed ()
   {
      let result = "";
      let tempResult = this.#lastplay.replaceAll (" ","");

      // check for lowercase as they will be a wild card * letter

      for (let i = 0;   i < tempResult.length; i++) {
         if ((tempResult.charCodeAt(i) >= 65) && (tempResult.charCodeAt(i) <= 90)) { result += tempResult [i];  }
         else { result += "*"; }
      }

      this.#lastplay  = blankStart;

      return result;
   }

   updateBoardWithLastPlayed (player_number)
   {
      let tempBoard = "";
      let tempPlayedBy = "";

      for (let i = 0; i < this.#lastplay.length; i++)
      {
         if (this.#lastplay [i] == " ") {
            tempBoard    += this.#thewords [i];
            tempPlayedBy += this.#playedby [i];
         }
         else {
            tempBoard += this.#lastplay [i];
            tempPlayedBy += player_number;
         }
      }

      let lettersPlayed = this.#lastplay.replace(/\s+/g, '');

      this.#lastplay = blankStart;
      this.#thewords    = tempBoard;
      this.#playedby = tempPlayedBy;

      return lettersPlayed;
   }

   removeBoard ()
   {
       if (this.#node != null) this.#node.innerHTML = '';
   }

   refreshTD (offset)
   {
       // instead of refreshing the whole board for one letter change maybe implement this function
       // if there is time
   }

   appendBoard (words, lastplay, playedby)
   {
      if (typeof words       !== 'undefined') this.#thewords    = words;
//      if (typeof retractable !== 'undefined') this.#retractable = retractable;
      if (typeof lastplay    !== 'undefined') this.#lastplay    = lastplay;
      if (typeof playedby    !== 'undefined') this.#playedby    = playedby;

      this.removeBoard ();

//      if (this.#node.hasChildNodes () == true) { this.#node.removeChild (this.#node.firstChild); }

      let i = 0;
      let trNode;

      while ( i < this.#length) {
         if ((i % this.#width) == 0) {
            trNode = this.createTRNode ("","");
            this.#node.appendChild (trNode);
         }

         trNode.appendChild (this.createTDNode ("board_td_",i));
         i++;
      }
   }




}