package yan.examples.yan.parse;

import yan.examples.yan.tree.YanTree;
import yan.skeleton.compiler.frontend.lex.LexerToken;
import yan.skeleton.compiler.frontend.parse.AbstractParser;
import yan.skeleton.driver.BaseConfig;

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
