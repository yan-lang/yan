package yan.foundation.compiler.frontend.parse;

import yan.foundation.compiler.frontend.lex.LexerToken;

import java.util.List;

public interface Parser<Out> {
    Out parse(List<LexerToken> tokens);
}
