package yan.lang;

import yan.common.error.lex.UnknownTokenError;
import yan.foundation.compiler.frontend.lex.AbstractLexer;
import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.compiler.frontend.lex.Vocabulary;
import yan.foundation.driver.BaseConfig;
import yan.lang.predefine.YanTokens;


public class YanLexer extends AbstractLexer implements YanTokens {

    public YanLexer(String name, BaseConfig config) {
        super(name, config, new Vocabulary(tokenNames));
    }

    @Override
    public Token nextToken() {
        skipWhitespace();
        markCurrentPos();
        if (buffer.current('\0')) return makeEOF();
        if (buffer.current('\n')) return makeToken(NEWLINE);
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
            errorCollector.addError(new UnknownTokenError(this));
        }

        return token;
    }

    private Token identifier() {
        while (Character.isLetterOrDigit(buffer.current()) || buffer.current() == '_') buffer.consume();
        if (keywords.containsKey(currentTokenString()))
            return makeToken(keywords.get(currentTokenString()));
        return makeToken(IDENTIFIER, currentTokenString());
    }

    private Token number() {
        while (Character.isDigit(buffer.current())) buffer.consume();
        return makeToken(INT_CONST, Integer.parseInt(currentTokenString()));
    }

    private void skipWhitespace() {
        while (buffer.current(' ', '\t', '\r')) ;
    }

}
