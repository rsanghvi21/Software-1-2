import components.map.Map;
import components.program.Program;
import components.program.Program1;
import components.queue.Queue;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.statement.Statement;
import components.utilities.Reporter;
import components.utilities.Tokenizer;

/**
 * Layered implementation of secondary method {@code parse} for {@code Program}.
 *
 * @author Bernard Anom, Rahul Sanghvi
 *
 */
public final class Program1Parse1 extends Program1 {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Parses a single BL instruction from {@code tokens} returning the
     * instruction name as the value of the function and the body of the
     * instruction in {@code body}.
     *
     * @param tokens
     *            the input tokens
     * @param body
     *            the instruction body
     * @return the instruction name
     * @replaces body
     * @updates tokens
     * @requires <pre>
     * [<"INSTRUCTION"> is a prefix of tokens]  and
     *  [<Tokenizer.END_OF_INPUT> is a suffix of tokens]
     * </pre>
     * @ensures <pre>
     * if [an instruction string is a proper prefix of #tokens]  and
     *    [the beginning name of this instruction equals its ending name]  and
     *    [the name of this instruction does not equal the name of a primitive
     *     instruction in the BL language] then
     *  parseInstruction = [name of instruction at start of #tokens]  and
     *  body = [Statement corresponding to the block string that is the body of
     *          the instruction string at start of #tokens]  and
     *  #tokens = [instruction string at start of #tokens] * tokens
     * else
     *  [report an appropriate error message to the console and terminate client]
     * </pre>
     */
    private static String parseInstruction(Queue<String> tokens, Statement body) {
        assert tokens != null : "Violation of: tokens is not null";
        assert body != null : "Violation of: body is not null";
        assert tokens.length() > 0 && tokens.front().equals("INSTRUCTION")
                : "" + "Violation of: <\"INSTRUCTION\"> is proper prefix of tokens";

        // Instruction keyword
        String instr = tokens.dequeue();
        Reporter.assertElseFatalError(instr.equals("INSTRUCTION"),
                "Expected 'INSTRUCTION' keyword.");

        // Instruction Name
        String s = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isIdentifier(s),
                "Invalid instruction name: " + s);

        // Checks for IS
        Reporter.assertElseFatalError(tokens.dequeue().equals("IS"),
                "Expected 'IS' after instruction name.");

        // Parse Block
        body.parseBlock(tokens);

        // Checks for END
        Reporter.assertElseFatalError(tokens.dequeue().equals("END"),
                "Expected 'END' after instruction body.");

        // Checks endName equals original name
        String endName = tokens.dequeue();
        Reporter.assertElseFatalError(s.equals(endName),
                "Instruction name mismatch: started with '" + s + "' but ended with '"
                        + endName + "'.");

        return s;
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public Program1Parse1() {
        super();
    }

    /*
     * Public methods ---------------------------------------------------------
     */

    @Override
    public void parse(SimpleReader in) {
        assert in != null : "Violation of: in is not null";
        assert in.isOpen() : "Violation of: in.is_open";
        Queue<String> tokens = Tokenizer.tokens(in);
        this.parse(tokens);
    }

    @Override
    public void parse(Queue<String> tokens) {
        assert tokens != null : "Violation of: tokens is not null";
        assert tokens.length() > 0
                : "" + "Violation of: Tokenizer.END_OF_INPUT is a suffix of tokens";

        Map<String, Statement> c = this.newContext();
        Statement b = this.newBody();

        // Checks for PROGRAM
        String program = tokens.dequeue();
        Reporter.assertElseFatalError(program.equals("PROGRAM"),
                "Expected 'PROGRAM' keyword.");

        // Program Name
        String pn = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isIdentifier(pn),
                "Invalid program name: " + pn);

        // Checks for IS
        String is = tokens.dequeue();
        Reporter.assertElseFatalError(is.equals("IS"), "Expected 'IS' keyword.");

        while (tokens.front().equals("INSTRUCTION")) {

            Statement instr = this.newBody();
            String instrName = parseInstruction(tokens, instr);

            // Checks for duplicate names
            Reporter.assertElseFatalError(!c.hasKey(instrName), "Duplicate instruction");

            // checks if instruction name is lowercase
            Reporter.assertElseFatalError(instrName.equals(instrName.toLowerCase()),
                    "Instruction name must be lowercase: " + instrName);

            // Check if name is primative instruction:
            Reporter.assertElseFatalError(
                    !(instrName.equals("move") || instrName.equals("turnleft")
                            || instrName.equals("turnright") || instrName.equals("infect")
                            || instrName.equals("skip")),
                    "Instruction name cannot be a primitive BL instruction: "
                            + instrName);

            // If valid add it to context
            c.add(instrName, instr);
        }

        String begin = tokens.dequeue();

        // Checks for BEGIN.
        Reporter.assertElseFatalError(begin.equals("BEGIN"), "Expected 'BEGIN' keyword.");

        // Parse Block
        b.parseBlock(tokens);

        // Checks for END
        String end = tokens.dequeue();
        Reporter.assertElseFatalError(end.equals("END"), "Expected 'END' keyword.");

        // Checks if start name and end name are equivalent
        String epn = tokens.dequeue();
        Reporter.assertElseFatalError(epn.equals(pn),
                "Program name mismatch: started with " + pn + " but ended with " + epn);

        Reporter.assertElseFatalError(
                tokens.length() == 1 && tokens.front().equals("### END OF INPUT ###"),
                "The program should have ended.");

        this.setName(pn);
        this.swapBody(b);
        this.swapContext(c);

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
        out.print("Enter valid BL program file name: ");
        String fileName = in.nextLine();
        /*
         * Parse input file
         */
        out.println("*** Parsing input file ***");
        Program p = new Program1Parse1();
        SimpleReader file = new SimpleReader1L(fileName);
        Queue<String> tokens = Tokenizer.tokens(file);
        file.close();
        p.parse(tokens);
        /*
         * Pretty print the program
         */
        out.println("*** Pretty print of parsed program ***");
        p.prettyPrint(out);

        in.close();
        out.close();
    }

}
