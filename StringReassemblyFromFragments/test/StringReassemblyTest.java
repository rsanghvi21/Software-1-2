import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

public class StringReassemblyTest {

    /*
     * Combination tests
     */

    @Test
    public void testCombination_equalOverlap() {
        String str1 = "Rahul";
        String str2 = "Rahul";
        int overlap = 5;
        String expected = "Rahul";
        String result = StringReassembly.combination(str1, str2, overlap);
        assertEquals(expected, result);
    }

    @Test
    public void testCombination_partialOverlap() {
        String str1 = "Going";
        String str2 = "ingles";
        int overlap = 3;
        String expected = "Goingles";
        String result = StringReassembly.combination(str1, str2, overlap);
        assertEquals(expected, result);
    }

    @Test
    public void testCombination_noOverlap() {
        String str1 = "Rahul";
        String str2 = "Sanghvi";
        int overlap = 0;
        String expected = "RahulSanghvi";
        String result = StringReassembly.combination(str1, str2, overlap);
        assertEquals(expected, result);
    }

    /*
     * addToSetAvoidingSubstrings tests
     */

    @Test
    public void testAddToSetAvoidingSubstrings_noSubstring() {
        Set<String> strSet = new Set1L<>();
        strSet.add("vanilla");
        strSet.add("chocolate");
        String str = "strawberry";
        Set<String> expected = new Set1L<>();
        expected.add("vanilla");
        expected.add("chocolate");
        expected.add("strawberry");
        StringReassembly.addToSetAvoidingSubstrings(strSet, str);
        assertEquals(expected, strSet);
    }

    @Test
    public void testAddToSetAvoidingSubstrings_strIsASubstring() {
        Set<String> strSet = new Set1L<>();
        strSet.add("vanilla");
        strSet.add("chocolate");
        strSet.add("strawberry");
        String str = "nilla";
        Set<String> expected = new Set1L<>();
        expected.add("vanilla");
        expected.add("chocolate");
        expected.add("strawberry");
        StringReassembly.addToSetAvoidingSubstrings(strSet, str);
        assertEquals(expected, strSet);
    }

    @Test
    public void testAddToSetAvoidingSubstrings_hasSubstringOfStr() {
        Set<String> strSet = new Set1L<>();
        strSet.add("nilla");
        strSet.add("chocolate");
        strSet.add("strawberry");
        String str = "vanilla";
        Set<String> expected = new Set1L<>();
        expected.add("vanilla");
        expected.add("chocolate");
        expected.add("strawberry");
        StringReassembly.addToSetAvoidingSubstrings(strSet, str);
        assertEquals(expected, strSet);
    }

    /*
     * linesFromInput tests
     */
    @Test
    public void testLinesFromInput_noSubstrings() {
        SimpleReader input = new SimpleReader1L("noSubstrings.txt");
        Set<String> result = StringReassembly.linesFromInput(input);
        Set<String> expected = new Set1L<>();
        expected.add("Rahul");
        expected.add("Sanghvi");
        assertEquals(expected, result);
        input.close();
    }

    @Test
    public void testLinesFromInput_withSubstrings() {
        SimpleReader input = new SimpleReader1L("withSubstrings.txt");
        Set<String> result = StringReassembly.linesFromInput(input);
        Set<String> expected = new Set1L<>();
        expected.add("vanilla");
        expected.add("strawberry");
        expected.add("orange");
        assertEquals(expected, result);
        input.close();
    }

    /*
     * printWithLineSeparators tests
     */
    @Test
    public void testPrintWithLineSeparatorsWithTilda() {

        File temp = new File("testPrintWithLineSeparatorsWithTilda.txt");
        SimpleWriter out = new SimpleWriter1L(temp.getPath());
        String input = "Rahul~k~Sanghvi~likes~ice~cream";
        StringReassembly.printWithLineSeparators(input, out);
        out.close();
        String result = "";
        SimpleReader in = new SimpleReader1L(temp.getPath());
        boolean firstLine = true;
        while (!in.atEOS()) {
            String line = in.nextLine();
            if (!firstLine) {
                result += "\n";
            }
            result += line;
            firstLine = false;

        }
        in.close();
        String expected = "Rahul\nk\nSanghvi\nlikes\nice\ncream";
        assertEquals(expected, result);
    }

    @Test
    public void testPrintWithLineSeparatorsWithoutTilda() {

        File temp = new File("testPrintWithLineSeparatorsWithoutTilda.txt");
        SimpleWriter out = new SimpleWriter1L(temp.getPath());
        String input = "Rahul Sanghvi loves ice cream";
        StringReassembly.printWithLineSeparators(input, out);
        out.close();
        String result = "";
        SimpleReader in = new SimpleReader1L(temp.getPath());
        boolean firstLine = true;
        while (!in.atEOS()) {
            String line = in.nextLine();
            if (!firstLine) {
                result += "\n";
            }
            result += line;
            firstLine = false;

        }
        in.close();
        String expected = "Rahul Sanghvi loves ice cream";
        assertEquals(expected, result);
    }

}
