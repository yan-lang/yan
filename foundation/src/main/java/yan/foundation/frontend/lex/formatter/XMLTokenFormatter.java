package yan.foundation.frontend.lex.formatter;

import yan.foundation.driver.lang.Formatter;
import yan.foundation.frontend.lex.Token;
import yan.foundation.utils.printer.XMLPrinter;

import java.util.List;

public class XMLTokenFormatter implements Formatter<List<Token>> {
    @Override
    public String format(List<Token> tokens) {
        XMLPrinter printer = new XMLPrinter("tokens");
        for (Token token : tokens) {
            printer.openElement("token");
            printer.pushSimpleElement("index", String.valueOf(token.index));
            printer.pushSimpleElement("text", token.getEscapedText());
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
}
