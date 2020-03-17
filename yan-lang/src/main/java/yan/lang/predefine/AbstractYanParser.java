package yan.lang.predefine;

import yan.foundation.compiler.frontend.parse.AbstractParser;
import yan.foundation.driver.BaseConfig;
import yan.foundation.driver.PhaseFormatter;

import java.util.Optional;

public abstract class AbstractYanParser extends AbstractParser<YanTree.Program> implements YanTokens {
    PhaseFormatter<YanTree.Program> formatter = new ParseTreePrinter();

    public AbstractYanParser(String name, BaseConfig config) {
        super(name, config);
    }

    @Override
    public Optional<PhaseFormatter<? super YanTree.Program>> getFormatter() {
        return Optional.of(formatter);
    }

    protected void recovery() {
        while (!isAtEnd()) {
            if (previous().type == NEWLINE) return;
            if (current().type == KW_VAR) return;
            advance();
        }
    }
}
