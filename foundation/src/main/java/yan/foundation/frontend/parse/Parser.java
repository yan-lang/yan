package yan.foundation.frontend.parse;

import yan.foundation.frontend.lex.Token;

import java.util.List;

public interface Parser<Out> {
    Out parse(List<Token> tokens);
}
