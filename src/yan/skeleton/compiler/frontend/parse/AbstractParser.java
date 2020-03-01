package yan.skeleton.compiler.frontend.parse;

import yan.skeleton.compiler.frontend.ast.Tree;
import yan.skeleton.compiler.frontend.lex.LexerToken;
import yan.skeleton.driver.Config;
import yan.skeleton.driver.Phase;

import java.util.List;

public abstract class AbstractParser<Out> extends Phase<List<LexerToken>, Out> implements Parser<Out> {

    public AbstractParser(String name, Config config) {
        super(name, config);
    }

}
