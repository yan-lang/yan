package yan.foundation.compiler.frontend.lex.formatter;

import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.driver.PhaseFormatter;

import java.util.List;

public abstract class TokenFormatter implements PhaseFormatter<List<Token>> {
    @Override
    public String targetName() {
        return "lex";
    }
}
