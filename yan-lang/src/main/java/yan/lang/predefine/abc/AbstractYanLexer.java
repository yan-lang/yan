package yan.lang.predefine.abc;

import yan.foundation.compiler.frontend.lex.AbstractLexer;
import yan.foundation.compiler.frontend.lex.Vocabulary;
import yan.lang.predefine.YanTokens;

public abstract class AbstractYanLexer extends AbstractLexer implements YanTokens {
    public AbstractYanLexer() {
        super("YanLexer", new Vocabulary(tokenNames));
    }
}
