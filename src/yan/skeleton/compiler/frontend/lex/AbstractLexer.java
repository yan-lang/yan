package yan.skeleton.compiler.frontend.lex;


import yan.skeleton.compiler.frontend.ast.Position;
import yan.skeleton.driver.Config;
import yan.skeleton.driver.Phase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLexer extends Phase<String, List<LexerToken>> implements Lexer {

    protected ReadTextBuffer buffer;
    protected Position pos;

    public AbstractLexer(String name, Config config) {
        super(name, config);
    }

    protected Position currentPosition() {
        return new Position(buffer.line, buffer.col, buffer.offset);
    }

    protected Position previousPosition() {
        return new Position(buffer.previousLine, buffer.previousCol, buffer.offset);
    }

    protected void markCurrentPos() {
        pos = currentPosition();
    }

    protected void markPreviousPos() {
        pos = currentPosition();
    }

    protected LexerToken makeToken(int type) {
        return new LexerToken(type, null, pos);
    }

    protected LexerToken makeToken(int type, Object value) {
        return new LexerToken(type, value, pos);
    }

    protected String currentTokenString() {
        return buffer.substring(pos.offset, currentPosition().offset);
    }

    @Override
    public List<LexerToken> transform(String input) {
        List<LexerToken> tokenList = new ArrayList<>();
        try {
            buffer = new ReadTextBuffer(input);
            LexerToken token;
            do {
                token = lex();
                tokenList.add(token);
            } while (token.type != BasicTokens.EOF);
        } catch (IOException e) {
            //TODO: issue a error.
        }
        return tokenList;
    }

    @Override
    public String stringfy(List<LexerToken> output) {
        StringBuilder builder = new StringBuilder();
        output.forEach(token -> builder.append(token.toString()).append('\n'));
        return builder.toString();
    }
}
