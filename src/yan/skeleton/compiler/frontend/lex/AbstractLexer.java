package yan.skeleton.compiler.frontend.lex;


import yan.skeleton.driver.BaseConfig;
import yan.skeleton.driver.Phase;
import yan.skeleton.printer.PhasePrinter;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractLexer extends Phase<String, List<LexerToken>>
        implements Lexer, PhasePrinter<List<LexerToken>> {

    protected ReadTextBuffer buffer;

    public AbstractLexer(String name, BaseConfig config) {
        super(name, config);
        printer = this;
    }

    protected void markCurrentPos() {
        buffer.mark();
    }

    protected LexerToken makeToken(int type) {
        return makeToken(type, null);
    }

    protected LexerToken makeToken(int type, Object value) {
        return new LexerToken(type, buffer.marked_line, buffer.marked_col,
                buffer.marked_offset, buffer.offset, value, buffer);
    }

    protected String currentTokenString() {
        return buffer.getText(buffer.marked_offset, buffer.offset);
    }

    @Override
    public List<LexerToken> transform(String input) {
        List<LexerToken> tokenList = new ArrayList<>();
        buffer = new ReadTextBuffer(input, config.inputFile.getName());
        LexerToken token;
        do {
            token = nextToken();
            tokenList.add(token);
        } while (token.type != BasicTokens.EOF);
        return tokenList;
    }

    @Override
    public String toString(List<LexerToken> lexerTokens) {
        return "tokens";
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
