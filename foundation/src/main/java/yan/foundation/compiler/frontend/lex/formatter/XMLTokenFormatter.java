package yan.foundation.compiler.frontend.lex.formatter;

import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.utils.printer.XMLPrinter;

import java.util.List;

public class XMLTokenFormatter extends TokenFormatter {
    @Override
    public String toString(List<Token> tokens) {
        XMLPrinter printer = new XMLPrinter("Tokens");
        for (Token token : tokens) {
            printer.openElement("Token");
            printer.pushSimpleElement("Type", token.getTypeString());
            printer.pushSimpleElement("Source", token.source.getSourceName());
            printer.pushSimpleElement("Lexer", token.lexer.getClass().getSimpleName());
            printer.pushSimpleElement("Value", String.valueOf(token.value));
            printer.pushSimpleElement("Line", String.valueOf(token.line));
            printer.pushSimpleElement("Column", String.valueOf(token.col));
            printer.pushSimpleElement("Start", String.valueOf(token.start));
            printer.pushSimpleElement("Stop", String.valueOf(token.stop));
            printer.closeElement();
        }
        return printer.flush();
    }

    @Override
    public String fileExtension() {
        return "xml";
    }
}
