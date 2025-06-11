import java.util.Comparator;

import components.map.Map;
import components.map.Map1L;
import components.sequence.Sequence;
import components.sequence.Sequence1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * This program will ask the user for an input file, read the file, counts the
 * word occurrences, and outputs them into a well-formatted HTML document with a
 * table of the words and counts listed in alphabetical order.
 *
 * @author Rahul Sanghvi
 *
 */
public final class WordCounter {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private WordCounter() {

    }

    /**
     *
     */
    private static final class Alpha implements Comparator<String> {
        /**
         * This method compares 2 strings and determines which string is
         * lexicographically greater.
         *
         * @param s1
         *            the first string
         * @param s2
         *            the second string
         * @return 1 if {@code s1} is lexicographically larger than {@code s2}
         *         -1 if {@code s1} is lexicographically smaller than {@code s2}
         *         0 if {@code s1} is lexicographically equal to {@code s2}
         */
        @Override
        public int compare(String s1, String s2) {
            return s1.toLowerCase().compareTo(s2.toLowerCase());
        }
    }

    /**
     * This method prints the opening tags, header, title, and sets up the table
     * in the HMTL file.
     *
     * @param out
     *            HTML output stream
     * @param inputFileName
     *            name of user-inputed file name
     * @updates out.contents
     * @requires out.is_open
     */
    public static void generateHeaderAndTable(SimpleWriter out, String inputFileName) {
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Words Counted </title>");
        out.println("</head>");
        out.println("<body>");
        out.print("<font color=\"blue\">");
        out.println("<h1>Word Counts in " + inputFileName + "</h1>");
        out.print("<font color=\"black\">");
        out.println("<table border=\"1\">");
        out.println("<tr><th>Word</th><th>Count</th></tr>");
    }

    /**
     * This method prints words and its counts onto the table to the HMTL file.
     *
     * @param out
     *            HTML output stream
     * @param keys
     *            sequence containing the alphabetically sorted words
     * @param wordCount
     *            Map<String, Integer> containing occurrences for each word
     *
     * @updates out.contents
     * @requires out.is_open
     *
     */
    public static void generateWordCount(SimpleWriter out, Sequence<String> keys,
            Map<String, Integer> wordCount) {
        for (int i = 0; i < keys.length(); i++) {
            String word = keys.entry(i);
            int count = wordCount.value(word);
            out.print("<font color=\"blue\">");

            out.println("<tr><td><font color=\"red\">" + word
                    + "</td><td><font color=\"black\">" + count + "</td></tr>");
        }
    }

    /**
     * This method prints the closing tags to the HMTL file.
     *
     * @param out
     *            HTML output stream
     *
     * @updates out.contents
     * @requires out.is_open
     */
    public static void generateFooter(SimpleWriter out) {
        out.println("</table>");
        out.println("</body>");
        out.println("</html>");
    }

    /**
     * Returns the first "word" (maximal length string of characters not in
     * {@code separators}) or "separator string" (maximal length string of
     * characters in {@code separators}) in the given {@code text} starting at
     * the given {@code position}.
     *
     * @param text
     *            the {@code String} from which to get the word or separator
     *            string
     * @param position
     *            the starting index
     * @param separators
     *            the {@code Set} of separator characters
     * @return the first word or separator string found in {@code text} starting
     *         at index {@code position}
     * @requires 0 <= position < |text|
     * @ensures <pre>
     * nextWordOrSeparator =
     *   text[position, position + |nextWordOrSeparator|)  and
     * if entries(text[position, position + 1)) intersection separators = {}
     * then
     *   entries(nextWordOrSeparator) intersection separators = {}  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      intersection separators /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of separators  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of separators)
     * </pre>
     */
    private static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {
        assert text != null : "Violation of: text is not null";
        assert separators != null : "Violation of: separators is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";

        String x = "";
        int i = position;
        boolean isSeparator = separators.contains(text.charAt(i));
        while (i < text.length() && separators.contains(text.charAt(i)) == isSeparator) {
            x += text.charAt(i);
            i++;
        }
        return x;
    }

    /**
     * This method checks if the given string contains only separator
     * characters.
     *
     * @param str
     *            the string to check
     * @param separators
     *            the set of separator characters
     * @return true if the string consists entirely of separators, false
     *         otherwise
     */
    private static boolean isSeparator(String str, Set<Character> separators) {
        boolean isSeparatorOnly = true;

        for (int i = 0; i < str.length(); i++) {
            if (!separators.contains(str.charAt(i))) {
                isSeparatorOnly = false;
            }
        }

        return isSeparatorOnly;
    }

    /**
     * Sorts a sequence of strings using the given comparator.
     *
     * @param sequence
     *            the sequence to sort
     * @param comparator
     *            the comparator to use for sorting alphabetically
     * @updates sequence
     */
    private static void sortSequence(Sequence<String> sequence,
            Comparator<String> comparator) {
        for (int i = 0; i < sequence.length(); i++) {
            for (int j = i + 1; j < sequence.length(); j++) {
                if (comparator.compare(sequence.entry(i), sequence.entry(j)) > 0) {

                    // Swap elements if they are out of order
                    String temp = sequence.entry(i);
                    sequence.replaceEntry(i, sequence.entry(j));
                    sequence.replaceEntry(j, temp);
                }
            }
        }
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        Map<String, Integer> wordCounts = new Map1L<>();
        String text = "";

        // Prompt user for respective input and output files
        out.println("Enter the name of an input file: ");
        String inputFile = in.nextLine();

        out.println("Enter the name of the output HTML file: ");
        String outputFile = in.nextLine();

        SimpleReader reader = new SimpleReader1L(inputFile);
        SimpleWriter writer = new SimpleWriter1L(outputFile);

        // Read in inputFile into one string
        while (!reader.atEOS()) {
            text += reader.nextLine() + " ";
        }
        reader.close();

        // Load all separators in a set
        Set<Character> separators = new Set1L<>();
        String separatorChars = " ,.!?;:'\"()[]{}<>-_\n\t";
        for (int i = 0; i < separatorChars.length(); i++) {
            separators.add(separatorChars.charAt(i));
        }

        int index = 0;
        while (index < text.length()) {
            String wordOrSeparator = nextWordOrSeparator(text, index, separators);
            index += wordOrSeparator.length();

            if (!isSeparator(wordOrSeparator, separators)) {
                if (wordCounts.hasKey(wordOrSeparator)) {
                    // Increment the word count if it already exists in the map
                    int currentCount = wordCounts.value(wordOrSeparator);
                    wordCounts.remove(wordOrSeparator);
                    wordCounts.add(wordOrSeparator, currentCount + 1);
                } else {
                    // Otherwise add the word to the map with a count of 1
                    wordCounts.add(wordOrSeparator, 1);
                }
            }
        }

        // Sort the keys (words) using a Sequence
        Sequence<String> sortedKeys = new Sequence1L<>();
        for (Map.Pair<String, Integer> pair : wordCounts) {
            sortedKeys.add(sortedKeys.length(), pair.key());
        }

        // Sort the sequence using the comparator
        Comparator<String> comparator = new Alpha();
        sortSequence(sortedKeys, comparator);

        // Add all information into a formatted HTML file
        generateHeaderAndTable(writer, inputFile);
        generateWordCount(writer, sortedKeys, wordCounts);
        generateFooter(writer);

        in.close();
        out.close();

    }
}
