package yan.foundation.compiler.frontend.lex;


import yan.foundation.driver.BaseConfig;
import yan.foundation.driver.Phase;
import yan.foundation.printer.XMLTokenPrinter;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractLexer extends Phase<String, List<Token>>
        implements Lexer {
    protected Vocabulary vocabulary;

    protected ReadTextBuffer buffer;

    public AbstractLexer(String name, BaseConfig config) {
        super(name, config);
        printer = new XMLTokenPrinter();
        vocabulary = new Vocabulary();
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
        return new Token(type, buffer.marked_line, buffer.marked_col,
                buffer.marked_offset, buffer.offset, value, buffer, this);
    }

    protected String currentTokenString() {
        return buffer.getText(buffer.marked_offset, buffer.offset);
    }

    @Override
    public List<Token> transform(String input) {
        List<Token> tokenList = new ArrayList<>();
        buffer = new ReadTextBuffer(input, config.inputFile.getName());
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
}
