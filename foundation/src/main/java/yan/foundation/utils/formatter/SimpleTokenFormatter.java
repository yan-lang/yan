package yan.foundation.utils.formatter;

import yan.foundation.compiler.frontend.lex.Token;

import java.util.List;

public class SimpleTokenFormatter extends TokenFormatter {

    @Override
    public String toString(List<Token> tokens) {
        StringBuilder builder = new StringBuilder();
        tokens.forEach(x -> builder.append(x.toSimpleString()).append('\n'));
        return builder.toString();
    }

    @Override
    public String fileExtension() {
        return "txt";
    }
}
