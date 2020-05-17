package yan.foundation.frontend.lex;


import yan.foundation.driver.lang.Code;
import yan.foundation.driver.lang.Phase;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractLexer extends Phase<Code, List<Token>>
        implements Lexer {
    protected Vocabulary vocabulary;

    protected ReadTextBuffer buffer;

    protected int index;

    // TODO(3-20): optimize constructors

    public AbstractLexer() {
        super();
        vocabulary = new Vocabulary();
    }

    public AbstractLexer(String name) {
        super(name);
        vocabulary = new Vocabulary();
    }

    public AbstractLexer(String name, Vocabulary vocabulary) {
        super(name);
        this.vocabulary = vocabulary;
    }

    protected void markCurrentPos() {
        buffer.mark();
    }

    protected Token makeEOF() {
        return makeToken(Token.EOF);
    }

    protected Token makeToken(int type) {
        return makeToken(type, null);
    }

    protected Token makeToken(int type, Object value) {
        return new Token.Builder(type, index)
                .pos(buffer.marked_line, buffer.marked_col, buffer.marked_offset, buffer.offset)
                .value(value).source(buffer).lexer(this).build();
    }

    protected String currentTokenString() {
        return buffer.getText(buffer.marked_offset, buffer.offset);
    }

    @Override
    public List<Token> transform(Code input) {
        List<Token> tokenList = new ArrayList<>();
        buffer = new ReadTextBuffer(input.content, input.filename);
        index = 0;
        Token token;
        do {
            token = nextToken();
            tokenList.add(token);
            index += 1;
        } while (token.type != Token.EOF);
        return tokenList;
    }

    // --------------------------------- //
    // Implementation of Lexer interface //
    // --------------------------------- //

    @Override
    public Vocabulary getVocabulary() {
        return vocabulary;
    }

    @Override
    public int getLine() {
        return buffer.line;
    }

    @Override
    public int getColumn() {
        return buffer.col;
    }

    @Override
    public CodeSource getCodeSource() {
        return buffer;
    }
}
