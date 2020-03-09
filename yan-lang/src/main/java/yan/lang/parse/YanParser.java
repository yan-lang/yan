package yan.lang.parse;

import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.compiler.frontend.parse.AbstractParser;
import yan.foundation.driver.BaseConfig;
import yan.lang.tree.YanTree;

import java.util.List;

public class YanParser extends AbstractParser<YanTree.Program> {

    public YanParser(String name, BaseConfig config) {
        super(name, config);
    }

    @Override
    public YanTree.Program transform(List<Token> input) {
        return null;
    }

    @Override
    public YanTree.Program parse(List<Token> tokens) {
        return null;
    }
}
