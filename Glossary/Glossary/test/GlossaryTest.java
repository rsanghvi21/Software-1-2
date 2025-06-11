import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.map.Map;
import components.map.Map1L;
import components.queue.Queue;
import components.set.Set;

public class GlossaryTest {

    /*
     * Tests of isTerm()
     */

    @Test // Routine case
    public void testIsTerm_ValidTerm() {
        assertEquals(true, Glossary.isTerm("rahul"));
    }

    @Test // Routine case
    public void testIsTerm_InvalidTermWithSpace() {
        assertEquals(false, Glossary.isTerm("rahul sanghvi"));
    }

    @Test // Boundary Case
    public void testIsTerm_EmptyString() {
        assertEquals(false, Glossary.isTerm(""));
    }

    @Test // Boundary case
    public void testIsTerm_WhitespaceOnly() {
        assertEquals(false, Glossary.isTerm(" "));
    }

    @Test // Challenge Case with numbers
    public void testIsTerm_NumericTerm() {
        assertEquals(true, Glossary.isTerm("123"));
        assertEquals(false, Glossary.isTerm("123 apple"));
    }

    /*
     * Tests of isDef()
     */

    @Test // Routine
    public void testIsDef_ValidDefinition() {
        assertEquals(true, Glossary.isDef("Rahul Sanghvi plays tennis."));
    }

    @Test // Routine
    public void testIsDef_NoSpaces() {
        assertEquals(false, Glossary.isDef("rahul"));
    }

    @Test // Boundary Case
    public void testIsDef_EmptyString() {
        assertEquals(false, Glossary.isDef(""));
    }

    @Test // Challenge Case
    public void testIsDef_DefinitionWithMultipleSpaces() {
        assertEquals(true, Glossary.isDef("Rahul        K      Sanghvi      "));
    }

    @Test // Challenge Case
    public void testIsDef_DefinitionWithSpecialCharacters() {
        assertEquals(true, Glossary.isDef("Rahul Sanghvi!"));
        assertEquals(true, Glossary.isDef("Rahul Sanghvi?"));
    }

    @Test // Challenge Case
    public void testIsDef_OnlySpecialCharacters() {
        assertEquals(false, Glossary.isDef("!!!"));
    }

    /*
     * Tests of hyperlinkCreator()
     */

    @Test // Routine case
    public void testHyperlinkCreator() {
        assertEquals("<a href=\"rahul.html\">", Glossary.hyperlinkCreator("rahul"));
    }

    /*
     * Tests of sort()
     */

    @Test // Routine Case
    public void testSort() {

        Map<String, String> x = new Map1L<String, String>();
        x.add("sanghvi", "Blah");
        x.add("zaid", "blah");
        x.add("rahul", "Blah");

        Queue<String> sortedTerms = Glossary.sort(x);

        assertEquals("rahul", sortedTerms.dequeue());
        assertEquals("sanghvi", sortedTerms.dequeue());
        assertEquals("zaid", sortedTerms.dequeue());
        assertEquals(0, sortedTerms.length());
    }

    @Test // Challenge case
    public void testSort_AlreadySortedTerms() {

        Map<String, String> x = new Map1L<String, String>();
        x.add("Rahul", "Blah");
        x.add("Sanghvi", "blah");
        x.add("Zaid", "Blah");

        Queue<String> sortedTerms = Glossary.sort(x);

        assertEquals("Rahul", sortedTerms.dequeue());
        assertEquals("Sanghvi", sortedTerms.dequeue());
        assertEquals("Zaid", sortedTerms.dequeue());
    }

    @Test // Challenge
    public void testSort_ReverseSortedTerms() {
        Map<String, String> x = new Map1L<String, String>();

        x.add("Zaid", "Blah");
        x.add("Sanghvi", "blah");
        x.add("Rahul", "Blah");

        Queue<String> sortedTerms = Glossary.sort(x);

        assertEquals("Rahul", sortedTerms.dequeue());
        assertEquals("Sanghvi", sortedTerms.dequeue());
        assertEquals("Zaid", sortedTerms.dequeue());
    }

    @Test // Boundary case
    public void testSort_SingleTerm() {
        Map<String, String> x = new Map1L<String, String>();

        x.add("Zaid", "Blah");
        Queue<String> sortedTerms = Glossary.sort(x);

        assertEquals("Zaid", sortedTerms.dequeue());
    }

    @Test // Boundary Case
    public void testSort_EmptyDictionary() {
        Map<String, String> x = new Map1L<String, String>();
        Queue<String> sortedTerms = Glossary.sort(x);

        assertEquals(0, sortedTerms.length());
    }

    /*
     * Tests of getTermsInSentence()
     */
    @Test // Routine Case
    public void testGetTermsInSentence_WithTerms() {

        Map<String, String> x = new Map1L<String, String>();
        x.add("rahul", "blah");
        x.add("zaid", "blah");

        String def = "rahul and zaid are friends";
        Set<String> termsInSentence = Glossary.getTermsInSentence(x, def);
        assertEquals(true, termsInSentence.contains("rahul"));
        assertEquals(true, termsInSentence.contains("zaid"));
    }

    @Test // Routine Case
    public void testGetTermsInSentence_NoTerms() {
        Map<String, String> x = new Map1L<String, String>();
        x.add("rahul", "blah");
        x.add("zaid", "blah");

        String def = "Lebron james is the GOAT";
        Set<String> termsInSentence = Glossary.getTermsInSentence(x, def);
        assertEquals(false, termsInSentence.contains("rahul"));
        assertEquals(false, termsInSentence.contains("zaid"));
        assertEquals(0, termsInSentence.size());
    }

    @Test // Challenge Case
    public void testGetTermsInSentence_TermAppearsMultipleTimes() {
        Map<String, String> x = new Map1L<String, String>();
        x.add("Rahul", "blah.");
        String definition = "Rahul knows Rahul";

        Set<String> termsInSentence = Glossary.getTermsInSentence(x, definition);

        assertEquals(true, termsInSentence.contains("Rahul"));
    }

}
