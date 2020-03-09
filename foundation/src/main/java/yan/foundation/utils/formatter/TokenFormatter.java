package yan.foundation.utils.formatter;

import yan.foundation.compiler.frontend.lex.Token;

import java.util.List;

public abstract class TokenFormatter implements PhaseFormatter<List<Token>> {
    @Override
    public String targetName() {
        return "lex";
    }
}
