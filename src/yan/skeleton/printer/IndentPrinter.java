package yan.skeleton.printer;

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

    /**
     * Format print.
     *
     * @param fmt  format
     * @param args arguments
     */
    public void format(String fmt, Object... args) {
        write(String.format(fmt, args), false);
    }

    /**
     * Format print with end of line.
     *
     * @param fmt  format
     * @param args arguments
     */
    public void formatln(String fmt, Object... args) {
        write(String.format(fmt, args), true);
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
