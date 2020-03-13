package yan.foundation.compiler.frontend.lex;

import yan.foundation.utils.formatter.PhaseFormatter;

import java.util.List;

public abstract class TokenFormatter implements PhaseFormatter<List<Token>> {
    @Override
    public String targetName() {
        return "lex";
    }
}
