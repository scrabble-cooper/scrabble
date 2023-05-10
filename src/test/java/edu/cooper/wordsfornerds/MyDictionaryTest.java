package edu.cooper.wordsfornerds;
import edu.cooper.wordsfornerds.MyDictionary;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test Dictionary class")
public class MyDictionaryTest {

    @Test
    @DisplayName("Get ID successfully")
    public void testMyDictionary() {
        MyDictionary thismydictionary = new MyDictionary();
        assertEquals(0, thismydictionary.getId());
    }


}