package yan.lang.parse;

import yan.foundation.compiler.frontend.lex.AbstractLexer;
import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.compiler.frontend.lex.Vocabulary;
import yan.foundation.driver.BaseConfig;
import yan.foundation.printer.SimpleTokenPrinter;

import static yan.lang.parse.YanTokens.*;


public class YanLexer extends AbstractLexer {

    public YanLexer(String name, BaseConfig config) {
        super(name, config);
        vocabulary = new Vocabulary(tokenNames);
        printer = new SimpleTokenPrinter();
    }

    @Override
    public Token nextToken() {
        skipWhitespace();
        markCurrentPos();
        if (buffer.current('\0')) return makeEOF();
        if (Character.isDigit(buffer.current())) return number();
        if (Character.isLetter(buffer.current()) || buffer.current() == '_') return identifier();

        Token token = switch (buffer.current()) {
            case '+' -> makeToken(PLUS);
            case '-' -> makeToken(MINUS);
            case '*' -> makeToken(buffer.peek('*') ? EXP : MULTI);
            case '/' -> makeToken(DIV);
            case '=' -> makeToken(ASSIGN);
            default -> makeToken(UNKNOWN);
        };
        buffer.consume();

        if (token.type == UNKNOWN) {
            // TODO: issue error
        }

        return token;
    }

    private Token identifier() {
        while (Character.isLetterOrDigit(buffer.current()) || buffer.current() == '_') buffer.consume();
        return makeToken(IDENTIFIER, currentTokenString());
    }

    private Token number() {
        while (Character.isDigit(buffer.current())) buffer.consume();
        return makeToken(INT_CONST, Integer.parseInt(currentTokenString()));
    }

    private void skipWhitespace() {
        while (buffer.current(' ', '\t', '\n', '\r')) ;
    }

}
