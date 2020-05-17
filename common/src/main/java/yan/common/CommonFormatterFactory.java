package yan.common;

import yan.foundation.driver.lang.Formatter;
import yan.foundation.frontend.lex.Token;

import java.util.List;

public interface CommonFormatterFactory<TopLevel> {
    Formatter<List<Token>> lex(boolean isInterpreting);

    Formatter<TopLevel> parse(boolean isInterpreting);

    Formatter<TopLevel> cs(boolean isInterpreting);

    Formatter<TopLevel> nameResolve(boolean isInterpreting);

    Formatter<TopLevel> typeCheck(boolean isInterpreting);
}
