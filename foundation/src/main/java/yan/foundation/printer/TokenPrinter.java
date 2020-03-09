package yan.foundation.printer;

import yan.foundation.compiler.frontend.lex.Token;

import java.util.List;

public abstract class TokenPrinter implements PhasePrinter<List<Token>> {
    @Override
    public String targetName() {
        return "lex";
    }
}
