import components.queue.Queue;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.statement.Statement;
import components.statement.Statement1;
import components.utilities.Reporter;
import components.utilities.Tokenizer;

/**
 * Layered implementation of secondary methods {@code parse} and
 * {@code parseBlock} for {@code Statement}.
 *
 * @author Bernard Anom, Rahul Sanghvi
 *
 */
public final class Statement1Parse1 extends Statement1 {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Converts {@code c} into the corresponding {@code Condition}.
     *
     * @param c
     *            the condition to convert
     * @return the {@code Condition} corresponding to {@code c}
     * @requires [c is a condition string]
     * @ensures parseCondition = [Condition corresponding to c]
     */
    private static Condition parseCondition(String c) {
        assert c != null : "Violation of: c is not null";
        assert Tokenizer.isCondition(c) : "Violation of: c is a condition string";
        return Condition.valueOf(c.replace('-', '_').toUpperCase());
    }

    /**
     * Parses an IF or IF_ELSE statement from {@code tokens} into {@code s}.
     *
     * @param tokens
     *            the input tokens
     * @param s
     *            the parsed statement
     * @replaces s
     * @updates tokens
     * @requires <pre>
     * [<"IF"> is a prefix of tokens]  and
     *  [<Tokenizer.END_OF_INPUT> is a suffix of tokens]
     * </pre>
     * @ensures <pre>
     * if [an if string is a proper prefix of #tokens] then
     *  s = [IF or IF_ELSE Statement corresponding to if string at start of #tokens]  and
     *  #tokens = [if string at start of #tokens] * tokens
     * else
     *  [reports an appropriate error message to the console and terminates client]
     * </pre>
     */
    private static void parseIf(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0 && tokens.front().equals("IF")
                : "" + "Violation of: <\"IF\"> is proper prefix of tokens";

        //Check statement kind is valid name
        String kind = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isKeyword(kind),
                "Invalid Statement Kind; Expected 'IF' keyword:" + kind);

        //Check condition
        String condition = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isCondition(condition),
                "Invalid Condition:" + condition);
        //Parse the condition
        Condition c = parseCondition(condition);

        // Checks for THEN
        String then = tokens.dequeue();
        Reporter.assertElseFatalError(then.equals("THEN"),
                "Expected 'THEN' keyword:" + then);

        //check body
        Statement s1 = s.newInstance();
        s1.parseBlock(tokens);
        //If statment is an IF-ELSE
        if (tokens.front().equals("ELSE")) {
            String elseblock = tokens.dequeue();
            Reporter.assertElseFatalError(elseblock.equals("ELSE"),
                    "Expected 'IF' keyword:" + elseblock);
            Statement s2 = s.newInstance();
            s2.parseBlock(tokens);
            s.assembleIfElse(c, s1, s2);
        } else {
            s.assembleIf(c, s1);
        }

        //check END
        String end = tokens.dequeue();
        Reporter.assertElseFatalError(end.equals("END"), "Expected 'END' keyword:" + end);

        String endkind = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isKeyword(endkind),
                "Expected 'IF' keyword:" + endkind);
    }

    /**
     * Parses a WHILE statement from {@code tokens} into {@code s}.
     *
     * @param tokens
     *            the input tokens
     * @param s
     *            the parsed statement
     * @replaces s
     * @updates tokens
     * @requires <pre>
     * [<"WHILE"> is a prefix of tokens]  and
     *  [<Tokenizer.END_OF_INPUT> is a suffix of tokens]
     * </pre>
     * @ensures <pre>
     * if [a while string is a proper prefix of #tokens] then
     *  s = [WHILE Statement corresponding to while string at start of #tokens]  and
     *  #tokens = [while string at start of #tokens] * tokens
     * else
     *  [reports an appropriate error message to the console and terminates client]
     * </pre>
     */
    private static void parseWhile(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0 && tokens.front().equals("WHILE")
                : "" + "Violation of: <\"WHILE\"> is proper prefix of tokens";

        // Dequeue and verify WHILE keyword
        String kind = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isKeyword(kind),
                "Invalid Statement Kind; Expected 'WHILE' keyword: " + kind);

        // Parse condition
        String condition = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isCondition(condition),
                "Invalid Condition: " + condition);
        Condition c = parseCondition(condition);

        // Check for DO keyword
        String doCond = tokens.dequeue();
        Reporter.assertElseFatalError(doCond.equals("DO"),
                "Expected 'DO' keyword: " + doCond);

        // Parse body block
        Statement s1 = s.newInstance();
        s1.parseBlock(tokens);
        s.assembleWhile(c, s1);

        // Check for END
        String end = tokens.dequeue();
        Reporter.assertElseFatalError(end.equals("END"),
                "Expected 'END' keyword: " + end);

        // Check for closing WHILE keyword
        String endKind = tokens.dequeue();
        Reporter.assertElseFatalError(endKind.equals("WHILE"),
                "Expected 'WHILE' keyword after END: " + endKind);
    }

    /**
     * Parses a CALL statement from {@code tokens} into {@code s}.
     *
     * @param tokens
     *            the input tokens
     * @param s
     *            the parsed statement
     * @replaces s
     * @updates tokens
     * @requires [identifier string is a proper prefix of tokens]
     * @ensures <pre>
     * s =
     *   [CALL Statement corresponding to identifier string at start of #tokens]  and
     *  #tokens = [identifier string at start of #tokens] * tokens
     * </pre>
     */
    private static void parseCall(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0 && Tokenizer.isIdentifier(tokens.front())
                : "" + "Violation of: identifier string is proper prefix of tokens";

        String call = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isIdentifier(call), "Invalid identifier");
        s.assembleCall(call);
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public Statement1Parse1() {
        super();
    }

    /*
     * Public methods ---------------------------------------------------------
     */

    @Override
    public void parse(Queue<String> tokens) {
        assert tokens != null : "Violation of: tokens is not null";
        assert tokens.length() > 0
                : "" + "Violation of: Tokenizer.END_OF_INPUT is a suffix of tokens";

        Reporter.assertElseFatalError(
                Tokenizer.isKeyword(tokens.front())
                        || Tokenizer.isIdentifier(tokens.front()),
                "Invalid Statement: " + tokens.front());

        if (tokens.front().equals("IF")) {
            parseIf(tokens, this);
        } else if (tokens.front().equals("WHILE")) {
            parseWhile(tokens, this);
        } else {
            parseCall(tokens, this);
        }

    }

    @Override
    public void parseBlock(Queue<String> tokens) {
        assert tokens != null : "Violation of: tokens is not null";
        assert tokens.length() > 0
                : "" + "Violation of: Tokenizer.END_OF_INPUT is a suffix of tokens";

        while (!tokens.front().equals("END") && !tokens.front().equals("ELSE")
                && !tokens.front().equals("### END OF INPUT ###")) {
            Statement s = this.newInstance();
            s.parse(tokens);
            this.addToBlock(this.lengthOfBlock(), s);
        }

    }

    /*
     * Main test method -------------------------------------------------------
     */

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        /*
         * Get input file name
         */
        out.print("Enter valid BL statement(s) file name: ");
        String fileName = in.nextLine();
        /*
         * Parse input file
         */
        out.println("*** Parsing input file ***");
        Statement s = new Statement1Parse1();
        SimpleReader file = new SimpleReader1L(fileName);
        Queue<String> tokens = Tokenizer.tokens(file);
        file.close();
        s.parse(tokens); // replace with parseBlock to test other method
        /*
         * Pretty print the statement(s)
         */
        out.println("*** Pretty print of parsed statement(s) ***");
        s.prettyPrint(out, 0);

        in.close();
        out.close();
    }

}
