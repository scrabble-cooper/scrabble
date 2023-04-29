package edu.cooper.wordsfornerds;

import edu.cooper.wordsfornerds.DataTransferObject;

// Had to be called MyDictionary so not as to confuse with java.util.dictionary
public class MyDictionary implements DataTransferObject {

    public boolean wasFound;
    public String wordDefinition;
    public String theWord;

    {
        theWord        = "";
        wasFound       = false;
        wordDefinition = "";
    }

    public long getId() {
        return 0;
    } // forced to have this function to compile

    public void printDictionaryInfo () {
        System.out.print(this.theWord);
        System.out.print("     ");
        System.out.println(this.wordDefinition);
    }
}
