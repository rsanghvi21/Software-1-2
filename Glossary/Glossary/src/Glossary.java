import java.util.Comparator;

import components.map.Map;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * This program will create a group of HTML files which together create a
 * glossary. The glossary will have an index, terms and definition based on the
 * inputed .txt file.
 *
 * @author Rahul Sanghvi
 *
 */
public final class Glossary {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private Glossary() {
    }

    /**
     * The method outputs the header in the generated HTML file.
     *
     * @param out
     *            the output stream
     * @updates out.contents
     * @requires out.is_open
     */
    public static void printHeader(SimpleWriter out) {
        assert out != null : "Violation of: out is not null";
        assert out.isOpen() : "Violation of: out.is_open";

        out.print("<html>");
        out.print("\n");
        out.print("<head>");
        out.print("\n");
        out.print("<title>");
        out.print("Cy Burnett's Custom Glossary Index");
        out.println("</title>");
        out.print("\n");
        out.print("</head>");
        out.print("\n");
    }

    /**
     * The method outputs the body in the generated HTML file.
     *
     * @param terms
     *            Queue<String> which contains all of the terms
     * @param out
     *            the output stream
     * @updates out.contents
     * @requires out.is_open
     */
    public static void printBody(Queue<String> terms, SimpleWriter out) {
        assert out != null : "Violation of: out is not null";
        assert out.isOpen() : "Violation of: out.is_open";
        out.print("<body>");
        out.print("\n");
        out.print("<h2>");
        out.print("Cy Burnett's Custom Glossary Index");
        out.print("</h2>");
        out.print("\n");
        out.print("<hr />");
        out.print("\n");
        out.print("<h3>");
        out.print("Index");
        out.print("</h3>");
        out.print("\n");
        out.print("<ul>");
        out.print("\n");

        // Iterate map and create bullet points and hyperlink for each term
        for (String term : terms) {
            out.println("<li>" + hyperlinkCreator(term) + term + "</a></li>");
        }

        // Closing tags
        out.print("</ul>");
        out.print("\n");
        out.print("<body>");
        out.print("\n");
        out.print("</html>");
        out.print("\n");

        // Close SimpleWriter
        out.close();
    }

    /**
     * This method prints the starting page
     *
     * @param terms
     *            all terms with hyperlink
     * @param file
     *            where to print file
     */
    public static void printMainPage(Queue<String> terms, String file) {
        // New SimpleWriter object to read
        SimpleWriter out = new SimpleWriter1L(file + "/" + "index.html");

        // Call method to print the header
        printHeader(out);

        // Call method to print the body
        printBody(terms, out);
    }

    /**
     * This method generates hyperlinks for terms
     *
     * @param s
     *            term that needs hyperlink
     * @return string containing the term with a hyperlink
     */
    public static String hyperlinkCreator(String s) {
        // Structure of hyperlink
        String hyperlink = "<a href=\"" + s + ".html\">";
        return hyperlink;
    }

    /**
     * The prints the term footer with the specified format
     *
     * @param out
     *            the output stream
     * @param definition
     *            the definition
     *
     * @updates out.contents
     * @requires out.is_open
     */
    public static void printTermFooter(SimpleWriter out, String definition) {
        assert out != null : "Violation of: out is not null";
        assert out.isOpen() : "Violation of: out.is_open";
        out.print("<blockquote>");
        out.print(definition);
        out.print("</blockquote>");
        out.println("<hr />");
        out.print("<p>Return to ");
        out.print("<a href=\"index.html\">index</a>");
        out.println(".</p>");
        out.print("</body>");
        out.print("\n");
        out.print("</html>");

    }

    /**
     * This method checks to see if {@code s} is a single word and therefore is
     * a term
     *
     * @param s
     *            string needed to be checked if it is a single word
     * @return true if the word is a singular word
     */
    public static boolean isTerm(String s) {
        boolean term = true; // Will be returned
        if (s.length() == 0) {
            term = false;
        }
        // Check for a space
        else {
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == ' ') {
                    term = false;
                }
            }
        }
        return term;
    }

    /**
     * This method checks to see if {@code s} is a sentence and therefore is a
     * definition
     *
     * @param s
     *            string needed to be checked if it is a sentence
     * @return true if the string is a sentence
     */
    public static boolean isDef(String s) {
        boolean sentence = false; // Will be returned
        if (s.length() == 0) {
            sentence = false;
        }
        // Check for a space
        else {
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == ' ') {
                    sentence = true;
                }
            }
        }
        return sentence;
    }

    /**
     * This method will read in the terms and definitions from the inputed file
     * and put them into a Map<String, String>
     *
     * @param Dictionary
     *            the Map<String, String> contains all of the terms and
     *            definitions
     * @param in
     *            the reader which reads each line from the input file
     * @update Dictionary
     */
    public static void getData(Map<String, String> Dictionary, SimpleReader in) {
        // Key
        String term = "";
        // Value
        String definition = "";

        while (!in.atEOS()) {
            String temp = in.nextLine().trim();

            // If we find a term
            if (isTerm(temp)) {
                // If we already have a term and its definition, add it to the map
                if (!term.isEmpty() && !definition.isEmpty()) {
                    Dictionary.add(term, definition.trim());
                    // Reset
                    term = "";
                    definition = "";
                }
                // Set the new term
                term = temp;
            }
            // If the line is empty, it marks the end of a definition
            else if (temp.isEmpty()) {
                if (!term.isEmpty() && !definition.isEmpty()) {

                    Dictionary.add(term, definition.trim());
                    // Reset
                    term = "";
                    definition = "";
                }
            }
            // Otherwise, it's part of the definition
            else {
                if (!definition.isEmpty()) {
                    // Space between lines
                    definition += " ";
                }
                definition += temp;
            }
        }

        // Final check
        if (!term.isEmpty() && !definition.isEmpty()) {
            Dictionary.add(term, definition.trim());
        }
    }

    /**
     * This method gets all of the terms in a definition sentence and stores
     * them in a Set<String>
     *
     * @param allTerms
     *            a Map<String, String> that has all of the terms
     * @param value
     *            the definition
     *
     * @return a Set<String> that has all of terms that exist in the definition
     */
    public static Set<String> getTermsInSentence(Map<String, String> allTerms,
            String value) {

        // Set to be returned
        Set<String> temp = new Set1L<>();

        // Iterate the map
        for (Map.Pair<String, String> term : allTerms) {
            // Set the key
            String key = term.key();

            // Checks if the term is inside the definition
            if (value.contains(key)) {
                int index = value.indexOf(key);
                int len = key.length();

                // Checks to see if there is a space or comma after and before
                // respectively in the definition where the term is found
                // Then it adds it to the set
                boolean isBoundaryAfter = (index + len == value.length())
                        || (value.charAt(index + len) == ' '
                                || (value.charAt(index + len) == ','));
                boolean isBoundaryBefore = (index == 0) || (value.charAt(index - 1) == ' '
                        || value.charAt(index + len) == ',');

                if (isBoundaryBefore && isBoundaryAfter && isTerm(key)) {
                    temp.add(key);
                }
            }
        }
        return temp;
    }

    private static class Alpha implements Comparator<String> {
        /**
         * This method compares 2 strings and determines which String is
         * lexicographically greater.
         *
         * @param s1
         *            the first String
         * @param s2
         *            the second String
         * @return 1 if {@code str1} is lexicographically larger than
         *         {@code str2} -1 if {@code str1} is lexicographically smaller
         *         than {@code str2} 0 if @code str1} lexicographically equal to
         *         {@code str2}
         */
        @Override
        public int compare(String s1, String s2) {
            int result = 0;
            if (s1.compareTo(s2) > 0) {
                result = 1;
            } else if (s1.compareTo(s2) < 0) {
                result = -1;
            }
            return result;
        }
    }

    /**
     * This method takes all of the terms from the Map<String, String>, adds
     * them to a queue, and sorts them alphabetically using a Comparator object
     *
     * @param Dictionary
     *            the Map <String, String> that contains all terms and
     *            corresponding definition
     * @return A sorted Queue<String> that contains all terms sin alphabetical
     *         order
     */
    public static Queue<String> sort(Map<String, String> Dictionary) {

        // Queue to be returned
        Queue<String> alphaTerms = new Queue1L<String>();

        // Iterates map for each term, and adds it to the queue
        for (Map.Pair<String, String> term : Dictionary) {
            alphaTerms.enqueue(term.key());
        }
        // Creates comparator object from above)
        Comparator<String> AZ = new Alpha();
        // Sorts from A-Z
        alphaTerms.sort(AZ);
        return alphaTerms;
    }

    /**
     * The prints the term with the specified format
     *
     * @param out
     *            the output stream
     * @param term
     *            the term
     *
     * @updates out.contents
     * @requires out.is_open
     */
    public static void printTermHeaderBody(SimpleWriter out, String term) {
        assert out != null : "Violation of: out is not null";
        assert out.isOpen() : "Violation of: out.is_open";

        out.print("<html>");
        out.print("\n");
        out.print("<head>");
        out.print("\n");
        out.print("<title>");
        out.print(term);
        out.print("</title>");
        out.print("\n");
        out.print("</head>");
        out.print("\n");
        out.print("<body>");
        out.print("\n");
        out.print("<h2>");
        out.print("<b>");
        out.print("<i>");
        out.print("<font color=\"red\">");
        out.print(term);
        out.print("</font>");
        out.print("</i>");
        out.print("</b>");
        out.print("</h2>");
        out.print("\n");

    }

    /**
     * This method prints all terms to the folder specified
     *
     * @param Dictionary
     *            a Map<String, String> which holds the terms and definitions
     * @param filePath
     *            location to print files
     */
    public static void printTerms(Map<String, String> Dictionary, String filePath) {

        // Set used to get all of the terms in definition
        Set<String> terms = new Set1L<String>();

        // Iterate map
        for (Map.Pair<String, String> term : Dictionary) {
            String temp = filePath + "/" + term.key() + ".html";
            SimpleWriter out = new SimpleWriter1L(temp);

            // Print term's header and body
            printTermHeaderBody(out, term.key());

            String definition = term.value();

            // Call method to see if term is inside definition
            terms = getTermsInSentence(Dictionary, term.value());

            // Iterate all words inside set
            for (String words : terms) {
                // Set definition to include terms found inside of it
                String subFirstHalf = definition.substring(0, definition.indexOf(words));
                String subSecondHalf = definition
                        .substring(definition.indexOf(words) + words.length());
                String link = hyperlinkCreator(words);
                definition = subFirstHalf + link + words + "</a>" + subSecondHalf;
            }

            printTermFooter(out, definition);
            out.close();
        }
    }

    /**
     * Main method.
     *
     * @param args
     *            not used in program
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        // Prompts user for a text file
        out.println("Enter a .txt file with terms and definitions: ");
        String txtFile = in.nextLine();

        // Prompts user for a file path
        out.println("Enter a folder to create glossary: ");
        String filePath = in.nextLine();

        // Create new Simplereader object to read txtfile
        SimpleReader reader = new SimpleReader1L(txtFile);

        // Create new Map to store terms and definitions
        Map<String, String> tAndD = new Map1L<String, String>();

        // Reads data from the file, and puts it accordingly into the map
        getData(tAndD, reader);

        //out.println(tAndD.toString());

        // Queue for terms
        Queue<String> terms = new Queue1L<String>();

        // Gets alphabetized queue
        terms = sort(tAndD);

        // Prints page
        printMainPage(terms, filePath);
        printTerms(tAndD, filePath);

        /*
         * Close input and output streams
         */
        in.close();
        out.close();

        // Tests
        // "C:\Users\R\Documents\workspace\Glossary\glossarySample.txt"
        // "C:\Users\R\Documents\workspace\Glossary\multiLines.txt"

        // Folder
        //"C:\Users\R\Documents\workspace\Glossary"
    }
}
