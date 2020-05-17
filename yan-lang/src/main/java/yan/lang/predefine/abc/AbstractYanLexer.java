package yan.lang.predefine.abc;

import yan.foundation.frontend.lex.AbstractLexer;
import yan.foundation.frontend.lex.Vocabulary;
import yan.lang.predefine.YanTokens;

public abstract class AbstractYanLexer extends AbstractLexer implements YanTokens {
    public AbstractYanLexer() {
        super("YanLexer", new Vocabulary(tokenNames));
    }
}
