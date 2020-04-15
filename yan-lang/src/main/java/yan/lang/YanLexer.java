package yan.lang;

import yan.foundation.compiler.frontend.lex.Token;
import yan.lang.predefine.abc.AbstractYanLexer;


public class YanLexer extends AbstractYanLexer {

    @Override
    public Token nextToken() {
        skipWhitespace();
        markCurrentPos();
        if (buffer.current('\0')) return makeEOF();
        if (buffer.current('\n')) return makeToken(NEWLINE);
        if (Character.isDigit(buffer.current())) return number();
        if (Character.isLetter(buffer.current()) || buffer.current() == '_') return identifier();

        Token token = switch (buffer.consume()) {
            case '+' -> makeToken(PLUS);
            case '-' -> makeToken(buffer.current('>') ? ARROW : MINUS);
            case '*' -> makeToken(MULTI);
            case '/' -> makeToken(DIV);
            case '^' -> makeToken(EXP);

            case ':' -> makeToken(COLON);
            case ',' -> makeToken(COMMA);
            case '.' -> makeToken(DOT);
            case ';' -> makeToken(SEMICOLON);

            case '(' -> makeToken(LEFT_PAREN);
            case ')' -> makeToken(RIGHT_PAREN);
            case '{' -> makeToken(LEFT_BRACE);
            case '}' -> makeToken(RIGHT_BRACE);
            case '[' -> makeToken(RIGHT_BRACKET);
            case ']' -> makeToken(LEFT_BRACKET);

            case '=' -> makeToken(buffer.current('=') ? EQ : ASSIGN);
            case '!' -> makeToken(buffer.current('=') ? NEQ : REL_NOT);
            case '>' -> makeToken(buffer.current('=') ? GTE : GT);
            case '<' -> makeToken(buffer.current('=') ? LTE : LT);

            case '|' -> makeToken(buffer.current('|') ? REL_OR : UNKNOWN);
            case '&' -> makeToken(buffer.current('&') ? REL_AND : UNKNOWN);

            default -> makeToken(UNKNOWN);
        };

        if (token.type == UNKNOWN) {
//            log.addError(new UnknownTokenError(this));
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
