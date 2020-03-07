package yan.foundation.printer;

import yan.foundation.compiler.frontend.lex.Lexer;
import yan.foundation.compiler.frontend.lex.LexerToken;

import java.util.List;

public class TokenPrinter implements PhasePrinter<List<LexerToken>> {

    protected Lexer lexer;

    public TokenPrinter(Lexer lexer) {
        this.lexer = lexer;
    }

    @Override
    public String toString(List<LexerToken> lexerTokens) {
        StringBuilder builder = new StringBuilder();
        lexerTokens.forEach(x -> builder.append(x.toString(lexer)).append('\n'));
        return builder.toString();
    }

    @Override
    public String targetName() {
        return "lex";
    }

    @Override
    public String fileExtension() {
        return "txt";
    }
}
