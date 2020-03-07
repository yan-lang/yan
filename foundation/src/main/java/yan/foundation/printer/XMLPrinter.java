package yan.foundation.printer;

import java.io.PrintWriter;
import java.util.Stack;

/**
 * A Simple XML Printer for printing xml content.
 * See also: https://leethomason.github.io/tinyxml2/classtinyxml2_1_1_x_m_l_printer.html
 * <p>
 * Example:
 * <pre>
 *
 * </pre>
 * The output would be something like this:
 * <pre>
 * {@code
 * <note>
 *     <to>George</to>
 *     <from>John</from>
 *     <heading>Reminder</heading>
 *     <body>Don't forget the meeting!</body>
 * </note>
 * }</pre>
 */
public class XMLPrinter {

    // ================ Public API ================ //

    private IndentPrinter printer = new IndentPrinter(4);
    private Stack<String> elements = new Stack<>();

    public XMLPrinter(String root) {
        openElement(root);
    }

    public void openElement(String name) {
        openElement(name, false);
    }

    public void openElement(String name, boolean compact_mode) {
        elements.push(name);
        printer.format("<%s>", name);
        if (!compact_mode) printer.println();
    }

    public void closeElement(boolean compact_mode) {
        elements.pop();
    }

    public void pushAttribute(String name, String value) {

    }

    public void pushText(String text) {

    }

    public void pushComment(String text) {
        printer.println(String.format("<!-- %s --> ", text));
    }

    // =================== Implementation =================== //

    public String flush() {
        // TODO: validate
        return printer.flush();
    }

    public void flush(PrintWriter out) {
        out.print(flush());
    }

    private String escape(String text) {
        text = text.replace("&", "&amp;");
        text = text.replace("<", "&lt;");
        text = text.replace(">", "&gt;");
        text = text.replace("'", "&apos;");
        text = text.replace("\"", "&quot;");
        return text;
    }

}
