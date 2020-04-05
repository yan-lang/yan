package yan.lang;

import yan.common.error.lex.UnknownTokenError;
import yan.foundation.compiler.frontend.lex.AbstractLexer;
import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.compiler.frontend.lex.Vocabulary;
import yan.foundation.compiler.frontend.lex.formatter.SimpleTokenFormatter;
import yan.foundation.driver.BaseConfig;
import yan.foundation.driver.PhaseFormatter;
import yan.lang.predefine.YanTokens;

import java.util.List;
import java.util.Optional;


public class YanLexer extends AbstractLexer implements YanTokens {

    public YanLexer(String name, BaseConfig config) {
        super(name, config, new Vocabulary(tokenNames));
    }

    @Override
    public Optional<PhaseFormatter<? super List<Token>>> getFormatter() {
        return Optional.of(new SimpleTokenFormatter());
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
            case '-' -> makeToken(buffer.peek('>') ? ARROW : MINUS);
            case '*' -> makeToken(MULTI);
            case '/' -> makeToken(DIV);
            case '^' -> makeToken(EXP);

            case ':' -> makeToken(COLON);
            case ',' -> makeToken(COMMA);
            case '.' -> makeToken(DOT);

            case '(' -> makeToken(LEFT_PAREN);
            case ')' -> makeToken(RIGHT_PAREN);
            case '{' -> makeToken(LEFT_BRACE);
            case '}' -> makeToken(RIGHT_BRACE);
            case '[' -> makeToken(RIGHT_BRACKET);
            case ']' -> makeToken(LEFT_BRACKET);

            case '=' -> makeToken(buffer.peek('=') ? EQ : ASSIGN);
            case '!' -> makeToken(buffer.peek('=') ? NEQ : REL_NOT);
            case '>' -> makeToken(buffer.peek('=') ? GTE : GT);
            case '<' -> makeToken(buffer.peek('=') ? LTE : LT);

            case '|' -> makeToken(buffer.peek('|') ? REL_OR : UNKNOWN);
            case '&' -> makeToken(buffer.peek('&') ? REL_AND : UNKNOWN);

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
