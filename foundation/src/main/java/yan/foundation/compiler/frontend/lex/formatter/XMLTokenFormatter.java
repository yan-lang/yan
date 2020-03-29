package yan.foundation.compiler.frontend.lex.formatter;

import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.utils.printer.XMLPrinter;

import java.util.List;

public class XMLTokenFormatter extends TokenFormatter {
    @Override
    public String toString(List<Token> tokens) {
        XMLPrinter printer = new XMLPrinter("tokens");
        for (Token token : tokens) {
            printer.openElement("token");
            printer.pushSimpleElement("type", token.getTypeString());
            printer.pushSimpleElement("source", token.source.getSourceName());
            printer.pushSimpleElement("lexer", token.lexer.getClass().getSimpleName());
            printer.pushSimpleElement("value", String.valueOf(token.value));
            printer.pushSimpleElement("line", String.valueOf(token.line));
            printer.pushSimpleElement("column", String.valueOf(token.col));
            printer.pushSimpleElement("start", String.valueOf(token.start));
            printer.pushSimpleElement("stop", String.valueOf(token.stop));
            printer.closeElement();
        }
        return printer.flush();
    }

    @Override
    public String fileExtension() {
        return "xml";
    }
}
