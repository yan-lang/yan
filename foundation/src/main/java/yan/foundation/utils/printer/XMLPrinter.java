package yan.foundation.utils.printer;

import java.io.PrintWriter;
import java.util.*;

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
    private Stack<Element> elements = new Stack<>();

    public XMLPrinter(String root, boolean compact_mode) {
        openElement(root, compact_mode);
    }

    public XMLPrinter(String root) {
        openElement(root, false);
    }

    public void openElement(String name) {
        openElement(name, false);
    }

    public void openElement(String name, boolean compact_mode) {
        elements.push(new Element(name, compact_mode));
        elements.peek();
    }

    public void closeElement() {
        Element element = elements.pop();
        elements.peek().addChild(element);
    }

    public void pushAttribute(String name, String value) {
        elements.peek().addAttribute(name, value);
    }

    public void pushText(String text) {
        elements.peek().setText(escape(text));
    }

    public void pushSimpleElement(String name, String text) {
        openElement(name, true);
        pushText(text);
        closeElement();
    }

    public String flush() {
        // Simple validation
        if (elements.size() > 1)
            throw new RuntimeException(String.format("You have unclosed elements <%s>. Please check your code.",
                    elements.peek().name));
        return elements.peek().toString(printer);
    }

    public void flush(PrintWriter out) {
        out.print(flush());
    }

    public String escape(String text) {
        text = text.replace("&", "&amp;");
        text = text.replace("<", "&lt;");
        text = text.replace(">", "&gt;");
        text = text.replace("'", "&apos;");
        text = text.replace("\"", "&quot;");
        return text;
    }

    public static class Element {
        String name;
        String text;
        Map<String, String> attributes = new HashMap<>();
        List<Element> children = new ArrayList<>();

        boolean isCompactMode;

        public Element(String name, boolean isCompactMode) {
            this.name = name;
            this.isCompactMode = isCompactMode;
        }

        public void addAttribute(String name, String value) {
            attributes.put(name, value);
        }

        public void addChild(Element child) {
            children.add(child);
        }

        public void setText(String text) {
            this.text = text;
        }

        public String toString(IndentPrinter printer) {
            /*
             <Note>
                Test
                <from>apple</from>
             <Note>

             start_compact:
                <Note>
                    apple</Note>
             */

            // Print start tag: <name key="value">
            StringBuilder attribute_str = new StringBuilder();
            attributes.forEach((key, value) -> attribute_str.append(String.format(" %s=\"%s\"", key, value)));
            printer.format("<%s%s>", name, attribute_str.toString());
            if (!isCompactMode) {
                printer.println();
                printer.indent();
            }

            // Print text
            if (text != null) {
                printer.print(text);
                if (!isCompactMode) printer.println();
            }

            // Print children
            for (var child : children) {
                child.toString(printer);
                if (!isCompactMode) printer.println();
            }

            // Print end tag: </name>
            if (!isCompactMode) {
                printer.unindent();
            }
            printer.format("</%s>", name);

            return printer.flush();
        }
    }

}
