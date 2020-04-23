package yan.common;

import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.driver.lang.Formatter;

import java.util.List;

public interface CommonFormatterFactory<TopLevel> {
    Formatter<List<Token>> clex();

    Formatter<List<Token>> ilex();

    Formatter<TopLevel> parse();

    Formatter<TopLevel> cs();

    Formatter<TopLevel> nameResolve();

    Formatter<TopLevel> typeCheck();
}
