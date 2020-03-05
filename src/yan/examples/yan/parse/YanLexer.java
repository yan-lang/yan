package yan.examples.yan.parse;

import yan.skeleton.compiler.frontend.lex.AbstractLexer;
import yan.skeleton.compiler.frontend.lex.LexerToken;
import yan.skeleton.compiler.frontend.lex.Vocabulary;
import yan.skeleton.driver.BaseConfig;

import static yan.examples.yan.parse.YanTokens.*;


public class YanLexer extends AbstractLexer {

    public YanLexer(String name, BaseConfig config) {
        super(name, config, new Vocabulary(tokenNames));
    }

    @Override
    public LexerToken nextToken() {
        skipWhitespace();
        markCurrentPos();
        if (buffer.current('\0')) return makeEOF();
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
        buffer.consume();

        if (token.type == UNKNOWN) {
            // TODO: issue error
        }

        return token;
    }

    private LexerToken identifier() {
        while (Character.isLetterOrDigit(buffer.current()) || buffer.current() == '_') buffer.consume();
        return makeToken(IDENTIFIER, currentTokenString());
    }

    private LexerToken number() {
        while (Character.isDigit(buffer.current())) buffer.consume();
        return makeToken(INT_CONST, Integer.parseInt(currentTokenString()));
    }

    private void skipWhitespace() {
        while (buffer.current(' ', '\t', '\n', '\r')) ;
    }

}
