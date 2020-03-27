package yan.foundation.compiler.frontend.lex;


import yan.foundation.compiler.frontend.lex.formatter.SimpleTokenFormatter;
import yan.foundation.compiler.frontend.lex.formatter.XMLTokenFormatter;
import yan.foundation.driver.BaseConfig;
import yan.foundation.driver.Phase;
import yan.foundation.driver.PhaseFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public abstract class AbstractLexer extends Phase<String, List<Token>>
        implements Lexer {
    protected Vocabulary vocabulary;

    protected ReadTextBuffer buffer;

    // TODO(3-20): optimize constructors

    public AbstractLexer() {
        super();
        shellFormatter = new SimpleTokenFormatter();
        vocabulary = new Vocabulary();
    }

    public AbstractLexer(String name, BaseConfig config) {
        super(name, config);
        shellFormatter = new SimpleTokenFormatter();
        vocabulary = new Vocabulary();
    }

    public AbstractLexer(String name, BaseConfig config, Vocabulary vocabulary) {
        super(name, config);
        shellFormatter = new SimpleTokenFormatter();
        this.vocabulary = vocabulary;
    }

    protected void markCurrentPos() {
        buffer.mark();
    }

    protected Token makeEOF() {
        return makeToken(Token.EOF);
    }

    protected Token makeToken(int type) {
        return makeToken(type, currentTokenString());
    }

    protected Token makeToken(int type, Object value) {
        return new Token(type, buffer.marked_line, buffer.marked_col,
                buffer.marked_offset, buffer.offset, value, buffer, this);
    }

    protected String currentTokenString() {
        return buffer.getText(buffer.marked_offset, buffer.offset);
    }

    @Override
    public Optional<PhaseFormatter<? super List<Token>>> getFormatter() {
        return Optional.of(new XMLTokenFormatter());
    }

    @Override
    public List<Token> transform(String input) {
        List<Token> tokenList = new ArrayList<>();
        buffer = new ReadTextBuffer(input, config.getInputSourceName());
        Token token;
        do {
            token = nextToken();
            tokenList.add(token);
        } while (token.type != Token.EOF);
        return tokenList;
    }

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
