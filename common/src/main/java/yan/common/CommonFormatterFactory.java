package yan.common;

import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.driver.lang.Formatter;

import java.util.List;

public interface CommonFormatterFactory<TopLevel> {
    Formatter<List<Token>> lex(boolean isInterpreting);

    Formatter<TopLevel> parse(boolean isInterpreting);

    Formatter<TopLevel> cs(boolean isInterpreting);

    Formatter<TopLevel> nameResolve(boolean isInterpreting);

    Formatter<TopLevel> typeCheck(boolean isInterpreting);
}
