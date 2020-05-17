package yan.foundation.frontend.lex.formatter;

import yan.foundation.driver.lang.Formatter;
import yan.foundation.frontend.lex.Token;
import yan.foundation.utils.ContentAligner;

import java.util.ArrayList;
import java.util.List;

public class SimpleTokenFormatter implements Formatter<List<Token>> {

    ContentAligner contentAligner = new ContentAligner();

    @Override
    public String format(List<Token> tokens) {
        List<String> block0 = new ArrayList<>();
        List<String> block1 = new ArrayList<>();
        List<String> block2 = new ArrayList<>();
        List<String> block3 = new ArrayList<>();
        List<String> block4 = new ArrayList<>();

        for (var token : tokens) {
            block0.add(String.format("%d. ", token.index));
            block1.add(token.getEscapedText());
            block2.add(token.getTypeString());
            block3.add(String.format("loc=%s:%d:%d;%d:%d", token.source.getSourceName(),
                                     token.line, token.col, token.start, token.stop));
            block4.add(String.format("val=\"%s\"", token.value));
        }

        return contentAligner.align(List.of(block0, block1, block2, block3, block4));
    }
}
