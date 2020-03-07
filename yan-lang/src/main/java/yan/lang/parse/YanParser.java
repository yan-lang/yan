package yan.lang.parse;

import yan.foundation.compiler.frontend.lex.LexerToken;
import yan.foundation.compiler.frontend.parse.AbstractParser;
import yan.foundation.driver.BaseConfig;
import yan.lang.tree.YanTree;

import java.util.List;

public class YanParser extends AbstractParser<YanTree.Program> {

    public YanParser(String name, BaseConfig config) {
        super(name, config);
    }

    @Override
    public YanTree.Program transform(List<LexerToken> input) {
        return null;
    }

    @Override
    public YanTree.Program parse(List<LexerToken> tokens) {
        return null;
    }
}
