package yan.lang.predefine;

import yan.foundation.compiler.frontend.lex.AbstractLexer;
import yan.foundation.compiler.frontend.lex.Vocabulary;

public abstract class AbstractYanLexer extends AbstractLexer implements YanTokens {
    public AbstractYanLexer() {
        super("YanLexer", new Vocabulary(tokenNames));
    }
}
