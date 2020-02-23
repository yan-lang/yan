package yan.skeleton.compiler.frontend.parse;

import yan.skeleton.compiler.frontend.ast.Tree;
import yan.skeleton.compiler.frontend.lex.LexerToken;

import java.util.List;

public interface Parser<Out> {
    Out parse(List<LexerToken> tokens);
}
