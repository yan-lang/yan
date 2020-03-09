package yan.foundation.printer;

import yan.foundation.compiler.frontend.lex.Token;

import java.util.List;

public class SimpleTokenPrinter extends TokenPrinter {

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
