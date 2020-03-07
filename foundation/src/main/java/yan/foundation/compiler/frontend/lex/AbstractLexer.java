package yan.foundation.compiler.frontend.lex;


import yan.foundation.driver.BaseConfig;
import yan.foundation.driver.Phase;
import yan.foundation.printer.TokenPrinter;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractLexer extends Phase<String, List<LexerToken>>
        implements Lexer {
    protected Vocabulary vocabulary;

    protected ReadTextBuffer buffer;

    public AbstractLexer(String name, BaseConfig config) {
        super(name, config);
        printer = new TokenPrinter(this);
        vocabulary = new Vocabulary();
    }

    protected void markCurrentPos() {
        buffer.mark();
    }

    protected LexerToken makeEOF() {
        return makeToken(LexerToken.EOF);
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
        } while (token.type != LexerToken.EOF);
        return tokenList;
    }

    @Override
    public Vocabulary getVocabulary() {
        return vocabulary;
    }
}
