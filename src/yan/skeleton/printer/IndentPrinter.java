package yan.skeleton.printer;

import java.io.PrintWriter;

/**
 * An useful printer for printing content with indention.
 *
 * Acknowledgement:
 *   Part of code of this class is borrowed from "decaf" Project.
 *   See: https://github.com/decaf-lang/decaf/blob/master/src/main/java/decaf/lowlevel/log/IndentPrinter.java
 */
public class IndentPrinter {

    public IndentPrinter(int indents) {
        this.indents = indents;
    }

    public IndentPrinter() {
        this(4);
    }

    public void indent() {
        spaces.append(" ".repeat(Math.max(0, indents)));
    }

    public void unindent() {
        spaces.setLength(spaces.length() - indents);
    }

    /**
     * Print a string.
     */
    public void print(String s) {
        write(s, false);
    }

    /**
     * Print a string, with end of line.
     */
    public void println(String s) {
        write(s, true);
    }

    /**
     * Print end of line.
     */
    public void println() {
        write("", true);
    }

    public String flush() {
        return content.toString();
    }

    public void flush(PrintWriter out) {
        out.print(flush());
    }

    private void write(String s, boolean endsLine) {
        if (isNewLine) {
            content.append(spaces.toString());
        }
        content.append(s);
        if (endsLine) {
            content.append('\n');
        }
        isNewLine = endsLine;
    }

    private int indents;
    private StringBuilder content = new StringBuilder();
    private StringBuilder spaces = new StringBuilder();
    private boolean isNewLine = true;
}