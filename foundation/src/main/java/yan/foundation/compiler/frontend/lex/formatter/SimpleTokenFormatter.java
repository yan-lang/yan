package yan.foundation.compiler.frontend.lex.formatter;

import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.driver.lang.Formatter;
import yan.foundation.utils.ContentAligner;

import java.util.ArrayList;
import java.util.List;

public class SimpleTokenFormatter implements Formatter<List<Token>> {

    ContentAligner contentAligner = new ContentAligner();

    @Override
    public String format(List<Token> tokens) {
        List<String> block1 = new ArrayList<>();
        List<String> block2 = new ArrayList<>();
        List<String> block3 = new ArrayList<>();

        for (var token : tokens) {
            block1.add(token.getTypeString());
            block2.add(String.format("- %s:%d:%d;%d:%d", token.source.getSourceName(),
                                     token.line, token.col, token.start, token.stop));
            block3.add(String.format("-> \"%s\"", token.value));
        }

        return contentAligner.align(List.of(block1, block2, block3));
    }
}
