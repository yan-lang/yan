package yan.foundation.compiler.frontend.parse;

import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.driver.BaseConfig;
import yan.foundation.driver.Phase;

import java.util.List;

public abstract class AbstractParser<Out> extends Phase<List<Token>, Out> implements Parser<Out> {

    public AbstractParser(String name, BaseConfig config) {
        super(name, config);
    }

}
