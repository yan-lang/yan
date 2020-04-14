package yan.foundation.utils.printer;

import java.io.PrintWriter;

/**
 * An useful printer for printing content with indention.
 *
 * <p>Acknowledgement: Part of code of this class is borrowed from "decaf" Project.</p>
 * <p>See: https://github.com/decaf-lang/decaf/blob/master/src/main/java/decaf/lowlevel/log/IndentPrinter.java.</p>
 */
public class IndentPrinter {

    // ==================== Public API ==================== //

    /**
     * Construct an IndentPrinter.
     *
     * @param indents The number of spaces per indent
     */
    public IndentPrinter(int indents) {
        this.indents = indents;
    }

    /**
     * Construct an IndentPrinter with indents = 4.
     */
    public IndentPrinter() {
        this(4);
    }

    public IndentPrinter indent() {
        spaces.append(" ".repeat(Math.max(0, indents)));
        return this;
    }

    public IndentPrinter unindent() {
        spaces.setLength(spaces.length() - indents);
        return this;
    }

    /**
     * Print a string.
     *
     * @param s string to be printed
     * @return current IndentPrinter object
     */
    public IndentPrinter print(String s) {
        write(s, false);
        return this;
    }

    /**
     * Print a string, with end of line.
     * @param s string to be printed
     * @return current IndentPrinter object
     */
    public IndentPrinter println(String s) {
        write(s, true);
        return this;
    }

    /**
     * Print end of line.
     * @return current IndentPrinter object
     */
    public IndentPrinter println() {
        write("", true);
        return this;
    }

    /**
     * Format print.
     *
     * @param fmt  format
     * @param args arguments
     * @return current IndentPrinter object
     */
    public IndentPrinter format(String fmt, Object... args) {
        write(String.format(fmt, args), false);
        return this;
    }

    /**
     * Format print with end of line.
     *
     * @param fmt  format
     * @param args arguments
     * @return current IndentPrinter object
     */
    public IndentPrinter formatln(String fmt, Object... args) {
        write(String.format(fmt, args), true);
        return this;
    }

    /**
     * Flush the content into a {@code String}.
     *
     * @return content
     */
    public String flush() {
        return content.toString();
    }

    /**
     * Flush the content to {@code out}.
     *
     * @param out The output stream.
     */
    public void flush(PrintWriter out) {
        out.print(flush());
    }

    // ==================== Implementation ==================== //

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
