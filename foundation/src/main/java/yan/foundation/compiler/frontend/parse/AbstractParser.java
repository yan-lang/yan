package yan.foundation.compiler.frontend.parse;

import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.driver.BaseConfig;
import yan.foundation.driver.Phase;
import yan.foundation.driver.error.Unexpected;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractParser<Out> extends Phase<List<Token>, Out> implements Parser<Out> {

    protected List<Token> tokens;
    protected int current;

    public AbstractParser(String name, BaseConfig config) {
        super(name, config);
    }

    @Override
    public Out transform(List<Token> input) {
        return parse(input);
    }

    @Override
    public Out parse(List<Token> tokens) {
        this.tokens = tokens;
        current = 0;
        return parse();
    }

    public abstract Out parse();

    // ------------------------------------------------ //
    //      Helper Functions for accessing tokens       //
    // ------------------------------------------------ //

    protected Token consume(int... types) throws Unexpected {
        for (int type : types) {
            if (check(type)) return advance();
        }
        throw new ExpectationError(types2Str(types), previous(), ExpectationError.AFTER);
    }

    protected boolean match(int... types) {
        for (int type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    protected boolean check(int type) {
        if (isAtEnd()) return false;
        return current().type == type;
    }

    protected Token current() {
        return tokens.get(current);
    }

    protected Token previous() {
        return tokens.get(current - 1);
    }

    protected Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    protected void back() {
        current -= 1;
    }

    protected boolean isAtEnd() {
        return current >= tokens.size();
    }

    // ------------------------------------------------ //
    //       Helper functions for throwing errors       //
    // ------------------------------------------------ //

    protected String type2Str(int tokenType) {
        return String.valueOf(tokenType);
    }

    protected String types2Str(int... tokenTypes) {
        List<String> strs = new ArrayList<>();
        for (int type : tokenTypes) {
            strs.add(type2Str(type));
        }
        return String.join(",", strs);
    }
}
