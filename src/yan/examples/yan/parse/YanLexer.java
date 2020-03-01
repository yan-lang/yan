package yan.examples.yan.parse;

import yan.skeleton.compiler.frontend.lex.AbstractLexer;
import yan.skeleton.compiler.frontend.lex.LexerToken;
import yan.skeleton.driver.Config;

import static yan.examples.yan.parse.YanTokens.*;


public class YanLexer extends AbstractLexer {

    public YanLexer(String name, Config config) {
        super(name, config);
    }

    @Override
    public LexerToken nextToken() {
        skipWhitespace();
        markCurrentPos();
        if (buffer.current('\0')) return makeToken(EOF);
        if (Character.isDigit(buffer.current())) return number();
        if (Character.isLetter(buffer.current()) || buffer.current() == '_') return identifier();

        LexerToken token = switch (buffer.current()) {
            case '+' -> makeToken(PLUS);
            case '-' -> makeToken(MINUS);
            case '*' -> makeToken(buffer.peek('*') ? EXP : MULTI);
            case '/' -> makeToken(DIV);
            case '=' -> makeToken(ASSIGN);
            default -> makeToken(UNKNOWN);
        };
        buffer.next();

        if (token.type == UNKNOWN) {
            // TODO: issue error
        }

        return token;
    }

    private LexerToken identifier() {
        while (Character.isLetterOrDigit(buffer.current()) || buffer.current() == '_') buffer.next();
        return makeToken(IDENTIFIER, currentTokenString());
    }

    private LexerToken number() {
        while (Character.isDigit(buffer.current())) buffer.next();
        return makeToken(INT_CONST, Integer.parseInt(currentTokenString()));
    }

    private void skipWhitespace() {
        while (buffer.current(' ', '\t', '\n', '\r')) ;
    }

}
