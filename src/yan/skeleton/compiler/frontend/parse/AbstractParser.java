package yan.skeleton.compiler.frontend.parse;

import yan.skeleton.compiler.frontend.lex.LexerToken;
import yan.skeleton.driver.BaseConfig;
import yan.skeleton.driver.Phase;

import java.util.List;

public abstract class AbstractParser<Out> extends Phase<List<LexerToken>, Out> implements Parser<Out> {

    public AbstractParser(String name, BaseConfig config) {
        super(name, config);
    }

}
